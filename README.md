<h1 align="center">
Crypto App for Encryption & Decryption with Biometric Authentication
</h1>

<br>

<div align="center">
<a name="code_factor" href="https://www.codefactor.io/repository/github/mina-mikhail/Biometric-With-Crypto">
  <img src="https://www.codefactor.io/repository/github/mina-mikhail/Biometric-With-Crypto/badge?style=for-the-badge">
</a>  
<a name="platform">
  <img src="https://img.shields.io/badge/Platform-Android-success?style=for-the-badge">
</a>
<a name="language">
  <img src="https://img.shields.io/badge/Language-Kotlin---?style=for-the-badge">
</a>
<a name="stars">
  <img src="https://img.shields.io/github/stars/Mina-Mikhail/Biometric-With-Crypto?style=for-the-badge"></a>
<a name="forks">
  <img src="https://img.shields.io/github/forks/Mina-Mikhail/Biometric-With-Crypto?logoColor=green&style=for-the-badge">
</a>
<a name="contributions">
  <img src="https://img.shields.io/github/contributors/Mina-Mikhail/Biometric-With-Crypto?logoColor=green&style=for-the-badge">
</a>
<a name="last_commit">
  <img src="https://img.shields.io/github/last-commit/Mina-Mikhail/Biometric-With-Crypto?style=for-the-badge">
</a>
<a name="issues">
  <img src="https://img.shields.io/github/issues-raw/Mina-Mikhail/Biometric-With-Crypto?style=for-the-badge">
</a>
<a name="license">
  <img src="https://img.shields.io/github/license/sadanandpai/javascript-code-challenges?style=for-the-badge">
</a>
<a name="linked_in" href="https://www.linkedin.com/in/minasamirgerges/">
  <img src="https://img.shields.io/badge/Support-Recommed%2FEndorse%20me%20on%20Linkedin-yellow?style=for-the-badge&logo=linkedin" alt="Recommend me on LinkedIn"/>
</a>
</div>

<br>
<br>

<div align="center">
<img src="https://github.com/Mina-Mikhail/Biometric-With-Crypto/blob/main/imgs/header-img.jpg">
</div>

<br>


:point_right: Extra Information:
-----------------

- [Biometric Authentication Presentation](https://docs.google.com/presentation/d/14h8hKSx8B_J4Sw21PWJYL-qh0ae6B4W7W_I543sU2no/edit?usp=sharing) :
  If you want to know more about biometrics and how to use/implement it, you can learn more from
  this presentation which created by me.

:point_right: Architecture:
-----------------

- Following Clean Architecture.
- Modularization.
- Applying SOLID principles, each class has a single job with separation of concerns by making
  classes independent
  of each other and communicating with interfaces.
- Using Kotlin-KTS & Gradle Version Catalog to handle project dependencies.

:point_right: Tech Stack & Libraries:
-----------------

- [MaterialDesign](https://m2.material.io/develop/android) : Use material design components in XML
  Design.
- [ViewBinding](https://developer.android.com/topic/libraries/view-binding) : Allows to more easily
  write code that interacts with views and replaces ```findViewById```.
- [Dagger-Hilt](https://developer.android.com/training/dependency-injection/hilt-android) : For
  dependency injection. Object creation and scoping is handled by Hilt.
- [Biometrics](https://developer.android.com/jetpack/androidx/releases/biometric) : To handle
  Biometric Authentication.
- [Preferences](https://developer.android.com/jetpack/androidx/releases/preference) : For Encrypted
  ShredPreferences.
- [Crypto](https://developer.android.com/privacy-and-security/cryptography) : For Encryption &
  Decryption.
- [Gson](https://github.com/google/gson) : For parsing and serializing objects.

:point_right: Project Structure:
-----------------

- Sample includes some basic screens to show how to implement biometric login with encryption &
  decryption for user data:
    - [Splash Screen](https://github.com/Mina-Mikhail/Biometric-With-Crypto/blob/main/app/src/main/java/com/minaMikhail/biometricWithCrypto/splash/SplashActivity.kt) :
      Handles biometric login and decrypt user data if user logged in before with credentials.
    - [Login Screen](https://github.com/Mina-Mikhail/Biometric-With-Crypto/blob/main/app/src/main/java/com/minaMikhail/biometricWithCrypto/login/LoginActivity.kt) :
      Handles user login business with credentials and encrypt his data.
    - [Home Screen](https://github.com/Mina-Mikhail/Biometric-With-Crypto/blob/main/app/src/main/java/com/minaMikhail/biometricWithCrypto/home/HomeActivity.kt) :
      Displays current user info, with benefit of logout and clear user data.

:point_right: Modules:
-----------------

- [biometricAuthentication](https://github.com/Mina-Mikhail/Biometric-With-Crypto/tree/main/biometricAuthentication) :
  To handle Biometric Authentication.
- [crypto](https://github.com/Mina-Mikhail/Biometric-With-Crypto/tree/main/crypto) : To handle
  Encryption & Decryption.
- [prefs](https://github.com/Mina-Mikhail/Biometric-With-Crypto/tree/main/prefs) : For managing
  SharedPreferences.

:point_right: Code Style:
-----------

- Following official kotlin code style

:point_right: Local Development:
-----------

- Here are some useful Gradle commands for executing this example:
    - `./gradlew clean` - Deletes build directory.

:point_right: Contributing to Project:
-----------

- Just fork this repository and contribute back using pull requests.
- Any contributions, large or small, major features, bug fixes, are welcomed and appreciated but
  will be thoroughly reviewed .

:point_right: Find this project useful ? :heart:
-----------

- Support it by clicking the :star: button on the upper right of this page. :v:

:point_right: Stargazers: :star:
-----------
[![Stargazers repo roster for @sadanandpai/javascript-code-challenges](https://reporoster.com/stars/Mina-Mikhail/Biometric-With-Crypto)](https://github.com/Mina-Mikhail/Biometric-With-Crypto/stargazers)


:point_right: Forkers: :hammer_and_pick:
-----------
[![Forkers repo roster for @sadanandpai/javascript-code-challenges](https://reporoster.com/forks/Mina-Mikhail/Biometric-With-Crypto)](https://github.com/Mina-Mikhail/Biometric-With-Crypto/network/members)


:point_right: Donation:
-----------
If this project help you reduce time to develop, you can give me a cup of coffee :)

<a href="https://www.buymeacoffee.com/mina.mikhail" target="_blank"><img src="https://bmc-cdn.nyc3.digitaloceanspaces.com/BMC-button-images/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: auto !important;width: auto !important;" ></a>

<br>
<br>

:warning: License:
--------

```
   Copyright (C) 2024 MINA MIKHAIL PRIVATE LIMITED

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
