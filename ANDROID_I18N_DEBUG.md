# Android i18n Debug Guide

## 🐛 Vấn đề: Android không nhận ngôn ngữ

### ✅ Đã Fix:

1. **Null Safety**: Fixed crash khi không truyền stickers
2. **Logging**: Thêm logging để debug
3. **String Resources**: Tạo file strings.xml cho tiếng Việt

---

## 📋 Cách kiểm tra Log

### 1. Chạy logcat để xem debug logs:

```bash
# Terminal 1 - Run logcat filter
cd example/android
adb logcat | grep -E "PhotoEditor"

# Terminal 2 - Run app
npm run android
```

### 2. Log messages bạn sẽ thấy:

```
PhotoEditorModule: Opening editor with language: vi
PhotoEditorModule: Language extra added: vi
PhotoEditorActivity: Received language: vi
PhotoEditorActivity: Setting locale to: vi
PhotoEditorActivity: Applying locale: vi_VN
PhotoEditorActivity: Locale applied successfully
PhotoEditorActivity: Total stickers: 123
```

---

## 🔍 Troubleshooting

### Issue 1: Log không hiển thị language

**Triệu chứng:**

```
PhotoEditorModule: Opening editor with language: null
```

**Giải pháp:**

- Kiểm tra code React Native có truyền `language` đúng không:

```javascript
const result = await PhotoEditor.open({
  path: 'https://...',
  language: 'vi', // ← Đảm bảo có dòng này
});
```

### Issue 2: Language được set nhưng text vẫn tiếng Anh

**Triệu chứng:**

```
PhotoEditorActivity: Applying locale: vi_VN
PhotoEditorActivity: Locale applied successfully
// Nhưng text trong app vẫn English
```

**Nguyên nhân:** Thiếu file strings.xml cho ngôn ngữ đó

**Giải pháp:** Tạo folder và file tương ứng:

- Tiếng Việt: `android/src/main/res/values-vi/strings.xml` ✅ (Đã tạo)
- Tiếng Trung (Giản thể): `values-zh-rCN/strings.xml`
- Tiếng Nhật: `values-ja/strings.xml`
- Tiếng Hàn: `values-ko/strings.xml`

### Issue 3: Crash khi không truyền stickers

**Triệu chứng:**

```
java.lang.NullPointerException
  at com.reactnativephotoeditor.PhotoEditorModule.open
```

**Giải pháp:** ✅ Đã fix! Giờ stickers là optional:

```kotlin
val stickers = options?.getArray("stickers")  // nullable
if (stickers != null) {
  intent.putExtra("stickers", stickers.toArrayList())
}
```

---

## 🧪 Test Steps

### Bước 1: Rebuild Android app

```bash
cd example/android
./gradlew clean
cd ..
npm run android
```

### Bước 2: Test với log

```bash
# Terminal 1
adb logcat -c  # Clear logs
adb logcat | grep PhotoEditor

# Terminal 2
npm run android
```

### Bước 3: Mở Photo Editor và quan sát

1. Tap vào ảnh để mở editor
2. Kiểm tra text trong UI:
   - **Nút Done** → Nên hiển thị "Xong"
   - **Save dialog** → "Bạn có muốn thoát mà không lưu ảnh?"
   - **Tool labels** → "Bút vẽ", "Tẩy", "Chữ", etc.

---

## 📝 String Resources đã tạo

### Tiếng Việt (`values-vi/strings.xml`) ✅

| Key              | English                                    | Tiếng Việt                          |
| ---------------- | ------------------------------------------ | ----------------------------------- |
| `label_brush`    | Brush                                      | Bút vẽ                              |
| `label_eraser`   | Eraser                                     | Tẩy                                 |
| `label_text`     | Text                                       | Chữ                                 |
| `label_filter`   | Filter                                     | Bộ lọc                              |
| `label_sticker`  | Sticker                                    | Nhãn dán                            |
| `done`           | Done                                       | Xong                                |
| `msg_save_image` | Are you want to exit without saving image? | Bạn có muốn thoát mà không lưu ảnh? |
| `save_error`     | Failed to save. Please try again!          | Lưu thất bại. Vui lòng thử lại!     |

---

## 🌍 Thêm ngôn ngữ mới

Để thêm ngôn ngữ mới, làm theo các bước:

### 1. Tạo folder tương ứng:

```bash
# Tiếng Trung Giản thể
mkdir -p android/src/main/res/values-zh-rCN

# Tiếng Nhật
mkdir -p android/src/main/res/values-ja

# Tiếng Hàn
mkdir -p android/src/main/res/values-ko

# Tiếng Pháp
mkdir -p android/src/main/res/values-fr

# Tiếng Đức
mkdir -p android/src/main/res/values-de
```

### 2. Copy file strings.xml:

```bash
cp android/src/main/res/values/strings.xml \
   android/src/main/res/values-vi/strings.xml
```

### 3. Dịch các string trong file

Mở file và dịch các string values (không dịch name attribute):

```xml
<!-- ❌ Sai -->
<string name="nút_xong">Done</string>

<!-- ✅ Đúng -->
<string name="done">Xong</string>
```

---

## ✅ Checklist

- [x] Fix null safety cho stickers
- [x] Thêm logging
- [x] Tạo setLocale() function
- [x] Map language codes to Locale
- [x] Tạo values-vi/strings.xml
- [ ] Test với tiếng Việt
- [ ] Tạo strings cho các ngôn ngữ khác (optional)

---

## 📞 Debug Commands

```bash
# Clear logcat
adb logcat -c

# Filter PhotoEditor logs only
adb logcat | grep "PhotoEditor"

# Filter by tag with priority
adb logcat PhotoEditorModule:D PhotoEditorActivity:D *:S

# Save logs to file
adb logcat | grep "PhotoEditor" > debug.log

# Check current locale
adb shell getprop persist.sys.locale

# Rebuild and reinstall
cd example/android && ./gradlew clean && cd .. && npm run android
```

---

## 🎯 Expected Result

Sau khi fix, khi bạn mở Photo Editor với `language: 'vi'`, bạn sẽ thấy:

1. **Logs**:

```
PhotoEditorModule: Opening editor with language: vi
PhotoEditorActivity: Setting locale to: vi
PhotoEditorActivity: Applying locale: vi_VN
```

2. **UI**:

- Nút "Done" → "Xong"
- Dialog text → Tiếng Việt
- Tool labels → Tiếng Việt

---

## 📚 Tham khảo

- [Android Localization Guide](https://developer.android.com/guide/topics/resources/localization)
- [Locale codes](https://developer.android.com/reference/java/util/Locale)
- [String Resources](https://developer.android.com/guide/topics/resources/string-resource)

---

**Last Updated:** 2024
**Status:** ✅ Fixed and Ready for Testing
