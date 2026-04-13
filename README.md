# Bunpou Notes (Android)

Aplikasi belajar tata bahasa Jepang N3 berbasis Android, fokus ke pengalaman belajar yang rapi, offline-first, dan UI modern. Proyek ini memakai Jetpack Compose, Room (SQLite), dan Supabase Auth untuk login.

Dokumen ini dibuat agar mudah diikuti bahkan untuk yang baru pertama kali pegang Android project.

---

## 1. Persiapan Lingkungan

Yang dibutuhkan:
- Android Studio (disarankan terbaru)
- JDK 17
- Android SDK (minimal API 26)
- `gradle` atau `./gradlew` (wrapper sudah ada)

Kalau baru pertama kali clone:
```bash
./gradlew tasks
```

---

## 2. Konfigurasi Wajib (local.properties)

File `local.properties` berisi secret lokal. Contoh minimal:

```properties
SUPABASE_URL=https://xxxxx.supabase.co
SUPABASE_ANON_KEY=your_supabase_anon_key
GOOGLE_WEB_CLIENT_ID=xxxxxxxxxxxx.apps.googleusercontent.com
```

Catatan:
- Jangan pakai tanda kutip.
- `GOOGLE_WEB_CLIENT_ID` harus Web Client ID dari Google Cloud (bukan Android Client ID).

---

## 3. Jalankan Aplikasi (Debug)

Lewat Android Studio:
1. Buka project.
2. Klik Run (▶).

Lewat terminal:
```bash
./gradlew assembleDebug
```

Install APK debug:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 4. Supabase Auth Setup

### 4.1 URL Configuration (Supabase)
Masuk Supabase Dashboard → Authentication → URL Configuration:

```
Site URL:
com.rjw.bunpoun3://login-callback

Redirect URLs:
com.rjw.bunpoun3://login-callback
com.rjw.bunpoun3://**
```

### 4.2 Google Provider (Supabase)
Authentication → Sign In / Providers → Google:

- `Client IDs` isi **Web Client ID terlebih dahulu**, lalu Android Client ID (pakai koma):
```
WEB_CLIENT_ID.apps.googleusercontent.com,ANDROID_CLIENT_ID.apps.googleusercontent.com
```
- `Client Secret` isi Web Client Secret.
- `Skip nonce checks` biarkan OFF.

---

## 5. Google OAuth Setup (Google Cloud)

### 5.1 Web OAuth Client
1. Google Cloud Console → APIs & Services → Credentials.
2. Create Credentials → OAuth client ID.
3. Application type: **Web application**.
4. Authorized redirect URI:
```
https://<your-project-ref>.supabase.co/auth/v1/callback
```
5. Copy Web Client ID + Web Client Secret.

### 5.2 Android OAuth Client
1. Create Credentials → OAuth client ID.
2. Application type: **Android**.
3. Package name:
```
com.rjw.bunpoun3
```
4. Isi SHA-1 sesuai build (debug/release).

---

## 6. Cara Dapat SHA-1

### 6.1 Debug SHA-1
```bash
./gradlew signingReport
```
Ambil SHA1 dari `Variant: debug`.

### 6.2 Release SHA-1
Kalau release sudah ter-sign:
```bash
apksigner verify --print-certs app/build/outputs/apk/release/app-release.apk
```

---

## 7. Build Release (Signed)

### 7.1 Konfigurasi Signing (local.properties)
Tambahkan:

```properties
RELEASE_STORE_FILE=/abs/path/to/keystore.jks
RELEASE_STORE_PASSWORD=your_store_password
RELEASE_KEY_ALIAS=your_key_alias
RELEASE_KEY_PASSWORD=your_key_password
```

Catatan penting:
- Keystore yang dibuat default via `keytool` umumnya **PKCS12**.
- Untuk PKCS12, **store password dan key password harus sama**.

### 7.2 Build
```bash
./gradlew clean assembleRelease
```

---

## 7.3 Signing di GitHub Actions (CI)

Agar release APK dari GitHub CI sudah signed, simpan keystore sebagai secret base64 dan isi secrets yang dibaca workflow.

### Langkah 1: Encode keystore ke Base64
Di lokal:
```bash
base64 -w 0 /abs/path/to/keystore.jks > keystore.base64
```

### Langkah 2: Tambahkan Secrets di GitHub
Masuk ke GitHub repo → Settings → Secrets and variables → Actions. Tambahkan:

```
RELEASE_KEYSTORE_BASE64=<isi keystore.base64>
RELEASE_STORE_PASSWORD=<password store>
RELEASE_KEY_ALIAS=<alias>
RELEASE_KEY_PASSWORD=<password key>
SUPABASE_URL=<supabase url>
SUPABASE_ANON_KEY=<supabase anon key>
GOOGLE_WEB_CLIENT_ID=<web client id>
```

### Langkah 3: Workflow akan otomatis
Workflow `Android Build` akan:
- decode keystore ke `keystore.jks`
- mengisi `local.properties`
- build `app-release.apk` sudah signed

---

## 8. Google Sign-In (Native)

Aplikasi memakai **Credential Manager** sehingga login Google tampil sebagai account picker native (bukan browser Chrome). Flow:
1. User pilih akun.
2. App ambil Google ID token.
3. ID token ditukar ke Supabase (`grant_type=id_token`).
4. Session disimpan lokal (offline-first).

---

## 9. Troubleshooting Umum

### 9.1 "Account reauth failed"
Biasanya karena SHA-1 tidak cocok.
- Pastikan Android OAuth Client dibuat dengan SHA-1 yang benar.
- Supabase `Client IDs` harus memasukkan Android Client ID.

### 9.2 "No credentials available"
Belum ada akun Google di device, atau OAuth client belum siap.
- Tambahkan akun Google di perangkat.
- Pastikan OAuth consent screen sudah `In production` atau email kamu ada di `Test users`.

### 9.3 Release build gagal "keystore password incorrect"
Pastikan `RELEASE_STORE_PASSWORD` benar dan cocok dengan PKCS12. Untuk PKCS12, **key password harus sama** dengan store password.

---

## 10. Struktur Project (Ringkas)

- `app/src/main/java/com/rjw/bunpoun3/MainActivity.kt`  
  UI utama, routing screen, auth flow.
- `app/src/main/java/com/rjw/bunpoun3/data/`  
  Repository, Room DB, Supabase auth.
- `app/src/main/assets/`  
  Data materi belajar (weeks, verb forms, romaji, dll).

---

## 11. Catatan Keamanan

`local.properties` berisi secret. Jangan commit ke git.  
Kalau mau share project, cukup share contoh format:

```properties
SUPABASE_URL=...
SUPABASE_ANON_KEY=...
GOOGLE_WEB_CLIENT_ID=...
RELEASE_STORE_FILE=...
RELEASE_STORE_PASSWORD=...
RELEASE_KEY_ALIAS=...
RELEASE_KEY_PASSWORD=...
```
