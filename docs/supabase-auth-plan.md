# Supabase Auth Plan

## Goal
Mengganti sistem `access_code.txt` lokal menjadi autentikasi berbasis Supabase Auth, sambil tetap menjaga pengalaman offline-first untuk materi dan progress belajar.

## Scope Awal
1. Tambahkan Supabase project dengan email magic link atau email/password.
2. Simpan session user secara lokal di perangkat.
3. Pertahankan Room/SQLite sebagai sumber data offline untuk materi, settings, dan progress.
4. Sinkronkan progress ke backend hanya saat user sudah login dan perangkat online.

## Arsitektur yang Disarankan
- `SupabaseAuthRepository`
  Mengelola sign-in, sign-out, refresh session, dan state user saat ini.
- `AppRepository`
  Tetap menjadi sumber data lokal untuk catalog, progress, settings, dan hasil kuis.
- `ProgressSyncWorker`
  Menjalankan sinkronisasi progress Room -> Supabase saat koneksi tersedia.

## Langkah Implementasi
### Phase 1: Auth Foundation
- Tambahkan dependency Supabase Kotlin client.
- Siapkan `BuildConfig` atau local properties untuk `SUPABASE_URL` dan `SUPABASE_ANON_KEY`.
- Buat screen `SignIn`.
- Ganti gate `access_code.txt` dengan `session != null`.

### Phase 2: User-linked Progress
- Tambahkan tabel backend `lesson_progress` dan `quiz_progress` dengan `user_id`.
- Simpan `updated_at` pada record lokal agar sinkronisasi dua arah lebih aman.
- Saat login berhasil, kirim local progress ke cloud lalu pull data terbaru.

### Phase 3: Offline-first Sync
- Tambahkan queue sinkronisasi atau flag `dirty`.
- Jalankan sync via WorkManager saat app start, login sukses, atau jaringan kembali tersedia.
- Terapkan strategi conflict resolution:
  record dengan `updated_at` terbaru menang.

## Perubahan Data yang Dibutuhkan
- Local Room:
  tambah kolom `updatedAt` dan `dirty` pada progress lokal.
- Supabase:
  tabel `lesson_progress`, `quiz_progress`, dan opsional `user_settings`.

## Catatan Migrasi
- Pertahankan fallback lokal saat user belum login.
- Sediakan satu kali migrasi:
  jika user punya progress lokal lama, upload ke akun pertama yang berhasil login.

## Risiko Teknis
- Konflik progress antar perangkat.
- Session expired saat offline lama.
- Anon key terekspos jika tidak dipisahkan dari secret server logic.

## Rekomendasi Berikutnya
- Implement `updatedAt` + `dirty` di Room lebih dulu.
- Setelah itu baru tambahkan dependency Supabase dan screen sign-in.
