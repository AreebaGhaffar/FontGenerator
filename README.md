# FontGenerator — Handwriting Font App 🖊️

An Android app that captures your unique handwriting style and converts any typed text into your own personalized font.

`Java` `Android` `Canvas API` `Image Processing`

## Screenshots

<img src="screenshots/home-screen.png" width="320"> <img src="screenshots/draw-letter.png" width="320"> <img src="screenshots/typing-preview.png" width="320">

## ✨ Features

- Draw all 26 letters (A–Z) on a touch canvas
- Save each letter as stroke path data
- Type any sentence and preview it in your handwriting
- Diary-style page rendering with ruled lines
- Apply Laplacian image sharpening filter
- Export final result as PNG image

## 🛠️ Tech Stack

- **Language:** Java
- **Min SDK:** API 24 (Android 7.0)
- **Drawing:** Android Canvas & Path API
- **Storage:** SharedPreferences (JSON)
- **Export:** MediaStore API
- **Image Processing:** 8-neighbor Laplacian Filter

## 📂 Project Structure

```
com.example.fontgenerator/
├── drawing/
│   ├── DrawingActivity.java
│   ├── DrawingView.java
│   └── LetterData.java
├── typing/
│   ├── TypingActivity.java
│   └── RenderView.java
├── processing/
│   └── LaplacianProcessor.java
└── utils/
    ├── StorageHelper.java
    └── ExportHelper.java
```

## 🔬 Image Processing

Uses the 8-neighbor Laplacian convolution mask:

```
[ -1 -1 -1 ]
[ -1  8 -1 ]
[ -1 -1 -1 ]
```

Formula: `output = original - laplacian`

## 🚀 Future Work

- PDF export
- Fallback system fonts for undrawn letters
- Multiple font folder management
- Natural handwriting variation
- Ink & paper color customization

## 👤 Author

Built by **Areeba Ghaffar**
GitHub: [@AreebaGhaffar](https://github.com/AreebaGhaffar)
