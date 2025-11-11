# Hướng dẫn sử dụng i18n với Photo Editor

## 🌍 Ngôn ngữ được hỗ trợ

Thư viện hỗ trợ các ngôn ngữ sau:

| Language Code | Language Name | iOS | Android |
| ------------- | ------------- | --- | ------- |
| `system`      | Theo hệ thống | ✅  | ✅      |
| `en`          | English       | ✅  | ✅      |
| `vi`          | Tiếng Việt    | ✅  | ✅      |
| `zh-Hans`     | 简体中文      | ✅  | ✅      |
| `zh-Hant`     | 繁體中文      | ✅  | ✅      |
| `ja`          | 日本語        | ✅  | ✅      |
| `fr`          | Français      | ✅  | ✅      |
| `de`          | Deutsch       | ✅  | ✅      |
| `ru`          | Русский       | ✅  | ✅      |
| `ko`          | 한국어        | ✅  | ✅      |
| `ms`          | Bahasa Melayu | ✅  | ✅      |
| `it`          | Italiano      | ✅  | ✅      |

> **Lưu ý:** Cả iOS và Android đều hỗ trợ set ngôn ngữ cụ thể. Nếu không truyền `language` hoặc truyền `'system'`, thư viện sẽ sử dụng ngôn ngữ hệ thống.

---

## 📝 Cách sử dụng

### 1. Cơ bản - Không truyền ngôn ngữ (dùng ngôn ngữ hệ thống)

```typescript
import PhotoEditor from '@baronha/react-native-photo-editor';

const result = await PhotoEditor.open({
  path: 'https://example.com/image.jpg',
});
```

### 2. Truyền ngôn ngữ cố định

```typescript
import PhotoEditor from '@baronha/react-native-photo-editor';

const result = await PhotoEditor.open({
  path: 'https://example.com/image.jpg',
  language: 'vi', // Tiếng Việt
});
```

### 3. Tích hợp với react-i18next

```typescript
import React from 'react';
import { useTranslation } from 'react-i18next';
import PhotoEditor, { LanguageType } from '@baronha/react-native-photo-editor';

function MyComponent() {
  const { i18n } = useTranslation();

  const openEditor = async (imagePath: string) => {
    try {
      // Map i18next language code to PhotoEditor language code
      const languageMap: Record<string, LanguageType> = {
        'en': 'en',
        'vi': 'vi',
        'zh-CN': 'zh-Hans',
        'zh-TW': 'zh-Hant',
        'ja': 'ja',
        'fr': 'fr',
        'de': 'de',
        'ru': 'ru',
        'ko': 'ko',
        'ms': 'ms',
        'it': 'it',
      };

      const photoEditorLanguage = languageMap[i18n.language] || 'system';

      const result = await PhotoEditor.open({
        path: imagePath,
        language: photoEditorLanguage,
      });

      console.log('Edited image:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    // Your UI here
  );
}
```

### 4. Tích hợp với react-native-localize

```typescript
import React from 'react';
import { getLocales } from 'react-native-localize';
import PhotoEditor, { LanguageType } from '@baronha/react-native-photo-editor';

function MyComponent() {
  const getPhotoEditorLanguage = (): LanguageType => {
    const locales = getLocales();
    const primaryLocale = locales[0];

    const languageMap: Record<string, LanguageType> = {
      'en': 'en',
      'vi': 'vi',
      'zh': primaryLocale.languageTag.includes('Hans') ? 'zh-Hans' : 'zh-Hant',
      'ja': 'ja',
      'fr': 'fr',
      'de': 'de',
      'ru': 'ru',
      'ko': 'ko',
      'ms': 'ms',
      'it': 'it',
    };

    return languageMap[primaryLocale.languageCode] || 'system';
  };

  const openEditor = async (imagePath: string) => {
    try {
      const result = await PhotoEditor.open({
        path: imagePath,
        language: getPhotoEditorLanguage(),
      });

      console.log('Edited image:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    // Your UI here
  );
}
```

### 5. Sử dụng với Context/Redux

```typescript
import React, { useContext } from 'react';
import PhotoEditor, { LanguageType } from '@baronha/react-native-photo-editor';

// Giả sử bạn có LanguageContext
const LanguageContext = React.createContext<{ language: string }>({ language: 'en' });

function MyComponent() {
  const { language } = useContext(LanguageContext);

  const openEditor = async (imagePath: string) => {
    try {
      const result = await PhotoEditor.open({
        path: imagePath,
        language: language as LanguageType,
        stickers: [
          'https://example.com/sticker1.png',
          'https://example.com/sticker2.png',
        ],
      });

      console.log('Edited image:', result);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    // Your UI here
  );
}
```

---

## 🔄 Mapping ngôn ngữ

Nếu bạn sử dụng các language codes khác với format của thư viện, bạn có thể tạo helper function:

```typescript
import { LanguageType } from '@baronha/react-native-photo-editor';

export function mapToPhotoEditorLanguage(languageCode: string): LanguageType {
  const mapping: Record<string, LanguageType> = {
    'en': 'en',
    'en-US': 'en',
    'en-GB': 'en',
    'vi': 'vi',
    'vi-VN': 'vi',
    'zh-CN': 'zh-Hans',
    'zh-Hans': 'zh-Hans',
    'zh-Hans-CN': 'zh-Hans',
    'zh-TW': 'zh-Hant',
    'zh-Hant': 'zh-Hant',
    'zh-Hant-TW': 'zh-Hant',
    'ja': 'ja',
    'ja-JP': 'ja',
    'fr': 'fr',
    'fr-FR': 'fr',
    'de': 'de',
    'de-DE': 'de',
    'ru': 'ru',
    'ru-RU': 'ru',
    'ko': 'ko',
    'ko-KR': 'ko',
    'ms': 'ms',
    'ms-MY': 'ms',
    'it': 'it',
    'it-IT': 'it',
  };

  return mapping[languageCode] || 'system';
}

// Sử dụng
const result = await PhotoEditor.open({
  path: imagePath,
  language: mapToPhotoEditorLanguage(i18n.language),
});
```

---

## 📱 Platform-specific notes

### iOS

- iOS cho phép set ngôn ngữ cụ thể cho photo editor
- Các text được hiển thị: "Cancel", "Done", "Undo", "Drag here to remove"
- Sử dụng `ZLImageEditorLanguageType` để set ngôn ngữ

### Android

- Android cũng hỗ trợ set ngôn ngữ cụ thể cho Activity
- Sử dụng `Locale` để thay đổi ngôn ngữ cho PhotoEditorActivity
- Text hiển thị phụ thuộc vào các string resources trong app
- Nếu không truyền `language` hoặc truyền `'system'`, sẽ dùng ngôn ngữ hệ thống

---

## 🎯 Complete Example

```typescript
import React, { useState } from 'react';
import { View, TouchableOpacity, Image, Text } from 'react-native';
import { useTranslation } from 'react-i18next';
import PhotoEditor, { LanguageType } from '@baronha/react-native-photo-editor';

export default function PhotoEditorExample() {
  const { i18n } = useTranslation();
  const [editedImage, setEditedImage] = useState<string | null>(null);

  const imageUrl =
    'https://images.unsplash.com/photo-1634915728822-5ad85582837a';

  const languageMap: Record<string, LanguageType> = {
    en: 'en',
    vi: 'vi',
    zh: 'zh-Hans',
    ja: 'ja',
    fr: 'fr',
    de: 'de',
    ru: 'ru',
    ko: 'ko',
  };

  const handleEdit = async () => {
    try {
      const currentLanguage = languageMap[i18n.language] || 'system';

      const result = await PhotoEditor.open({
        path: imageUrl,
        language: currentLanguage,
        stickers: [
          'https://cdn-icons-png.flaticon.com/512/5272/5272912.png',
          'https://cdn-icons-png.flaticon.com/512/5272/5272913.png',
        ],
      });

      setEditedImage(result);
      console.log('✅ Image saved:', result);
    } catch (error: any) {
      if (error?.code === 'USER_CANCELLED') {
        console.log('User cancelled editing');
      } else {
        console.error('Error editing image:', error);
      }
    }
  };

  return (
    <View style={{ flex: 1, padding: 20 }}>
      <TouchableOpacity onPress={handleEdit}>
        <Image
          source={{ uri: editedImage || imageUrl }}
          style={{ width: '100%', height: 300 }}
        />
        <Text style={{ textAlign: 'center', marginTop: 10 }}>
          Tap to edit (Language: {i18n.language})
        </Text>
      </TouchableOpacity>
    </View>
  );
}
```

---

## 🐛 Troubleshooting

### Language không thay đổi trên iOS

- Đảm bảo bạn đã rebuild app sau khi thay đổi code
- Kiểm tra giá trị `language` đang truyền vào có đúng không
- Thử dùng `'system'` để kiểm tra xem ngôn ngữ hệ thống có hoạt động không

### Language không thay đổi trên Android

- Rebuild app sau khi thay đổi code native
- Kiểm tra log để đảm bảo `language` được truyền đúng vào Intent
- Nếu dùng `'system'` hoặc không truyền `language`, app sẽ dùng ngôn ngữ hệ thống
- Text hiển thị trong PhotoEditor có thể phụ thuộc vào string resources của Android app

### Cả iOS và Android đều không đổi ngôn ngữ

- Đảm bảo giá trị language code hợp lệ (xem bảng supported languages ở trên)
- Log giá trị `language` trước khi gọi `PhotoEditor.open()` để kiểm tra
- Thử hardcode một ngôn ngữ cụ thể như `'vi'` hoặc `'en'` để test

---

## 📚 TypeScript Types

```typescript
export type LanguageType =
  | 'system'
  | 'en'
  | 'vi'
  | 'zh-Hans'
  | 'zh-Hant'
  | 'ja'
  | 'fr'
  | 'de'
  | 'ru'
  | 'ko'
  | 'ms'
  | 'it';

export type Options = {
  path: String;
  stickers?: Array<String>;
  language?: LanguageType;
};
```

---

Happy coding! 🎉
