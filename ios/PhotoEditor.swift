//
//  PhotoEditor.swift
//  PhotoEditor
//
//  Created by Donquijote on 27/07/2021.
//

import Foundation
import UIKit
import Photos
import SDWebImage
import AVFoundation
//import ZLImageEditor

public enum ImageLoad: Error {
    case failedToLoadImage(String)
}

@objc(PhotoEditor)
class PhotoEditor: NSObject, ZLEditImageControllerDelegate {
    var window: UIWindow?
    var bridge: RCTBridge!
    
    var resolve: RCTPromiseResolveBlock!
    var reject: RCTPromiseRejectBlock!
    
    @objc(open:withResolver:withRejecter:)
    func open(options: NSDictionary, resolve:@escaping RCTPromiseResolveBlock,reject:@escaping RCTPromiseRejectBlock) -> Void {
        
        // handle path
        guard let path = options["path"] as? String else {
            reject("DONT_FIND_IMAGE", "Dont find image", nil)
            return;
        }
        
        getUIImage(url: path) { image in
            DispatchQueue.main.async {
                //  set config
                self.setConfiguration(options: options, resolve: resolve, reject: reject)
                self.presentController(image: image)
            }
        } reject: {_ in
            reject("LOAD_IMAGE_FAILED", "Load image failed: " + path, nil)
        }
    }
    
    func onCancel() {
        self.reject("USER_CANCELLED", "User has cancelled", nil)
    }
    
    private func setConfiguration(options: NSDictionary, resolve:@escaping RCTPromiseResolveBlock,reject:@escaping RCTPromiseRejectBlock) -> Void{
        self.resolve = resolve;
        self.reject = reject;
        
        // Language configuration
        if let languageCode = options["language"] as? String {
            ZLImageEditorConfiguration.default().languageType = getLanguageType(from: languageCode)
        }
        
        // Stickers
        let stickers = options["stickers"] as? [String] ?? []
        ZLImageEditorConfiguration.default().imageStickerContainerView = StickerView(stickers: stickers)
        
        
        //Config
        ZLImageEditorConfiguration.default().editDoneBtnBgColor = UIColor(red:255/255.0, green:238/255.0, blue:101/255.0, alpha:1.0)

        // Custom colors palette - More colors
        let customColors: [UIColor] = [
            .white,                                     // Trắng
            .black,                                     // Đen
            zlRGB(241, 79, 79),                        // Đỏ
            zlRGB(243, 170, 78),                       // Cam
            zlRGB(255, 235, 59),                       // Vàng
            zlRGB(139, 195, 74),                       // Xanh lá nhạt
            zlRGB(80, 169, 56),                        // Xanh lá
            zlRGB(0, 150, 136),                        // Xanh lục lam
            zlRGB(30, 183, 243),                       // Xanh da trời
            zlRGB(33, 150, 243),                       // Xanh dương
            zlRGB(63, 81, 181),                        // Xanh đậm
            zlRGB(139, 105, 234),                      // Tím
            zlRGB(156, 39, 176),                       // Tím đậm
            zlRGB(233, 30, 99),                        // Hồng
            zlRGB(121, 85, 72),                        // Nâu
            zlRGB(158, 158, 158)                       // Xám
        ]
        
        ZLImageEditorConfiguration.default().editImageDrawColors = customColors
        ZLImageEditorConfiguration.default().textStickerTextColors = customColors
        
        ZLImageEditorConfiguration.default().editImageTools = [.draw, .clip, .textSticker]
        
        //Filters Lut
        do {
            let filters = ColorCubeLoader()
            ZLImageEditorConfiguration.default().filters = try filters.load()
        } catch {
            assertionFailure("\(error)")
        }
    }
    
    private func presentController(image: UIImage) {
        if let controller = UIApplication.getTopViewController() {
            controller.modalTransitionStyle = .crossDissolve
            
            ZLEditImageViewController.showEditImageVC(parentVC:controller , image: image, delegate: self) { [weak self] (resImage, editModel) in
                let documentsPath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0] as String
                
                let destinationPath = URL(fileURLWithPath: documentsPath).appendingPathComponent(String(Int64(Date().timeIntervalSince1970 * 1000)) + ".png")
                
                do {
                    try resImage.pngData()?.write(to: destinationPath)
                    self?.resolve(destinationPath.absoluteString)
                } catch {
                    debugPrint("writing file error", error)
                }
            }
        }
    }
    
    
    private func getUIImage (url: String ,completion:@escaping (UIImage) -> (), reject:@escaping(String)->()){
        if let path = URL(string: url) {
            SDWebImageManager.shared.loadImage(with: path, options: .continueInBackground, progress: { (recieved, expected, nil) in
            }, completed: { (downloadedImage, data, error, SDImageCacheType, true, imageUrlString) in
                DispatchQueue.main.async {
                    if(error != nil){
                        print("error", error as Any)
                        reject("false")
                        return;
                    }
                    if downloadedImage != nil{
                        completion(downloadedImage!)
                    }
                }
            })
        }else{
            reject("false")
        }
    }
    
    // Map language code to ZLImageEditorLanguageType
    private func getLanguageType(from code: String) -> ZLImageEditorLanguageType {
        switch code.lowercased() {
        case "en":
            return .english
        case "vi":
            return .vietnamese
        case "zh-hans", "zh-cn", "zh_hans":
            return .chineseSimplified
        case "zh-hant", "zh-tw", "zh_hant":
            return .chineseTraditional
        case "ja", "jp":
            return .japanese
        case "fr":
            return .french
        case "de":
            return .german
        case "ru":
            return .russian
        case "ko":
            return .korean
        case "ms":
            return .malay
        case "it":
            return .italian
        default:
            return .system
        }
    }
}

extension UIApplication {
    class func getTopViewController(base: UIViewController? = UIApplication.shared.keyWindow?.rootViewController) -> UIViewController? {
        
        if let nav = base as? UINavigationController {
            return getTopViewController(base: nav.visibleViewController)
        } else if let tab = base as? UITabBarController, let selected = tab.selectedViewController {
            return getTopViewController(base: selected)
        } else if let presented = base?.presentedViewController {
            return getTopViewController(base: presented)
        }
        
        return base
    }
}
