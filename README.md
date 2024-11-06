# Kiroku
## Kiroku is a Diary application where you can manage all your daily stories.

![5](https://github.com/user-attachments/assets/3bb39c1e-ae29-4f38-88b0-eaa0f40fce4d)
![6](https://github.com/user-attachments/assets/a3250b63-f904-4b2e-b94e-63debe9e4d31)

## Architecure
The app follows MVVM architecture
## Cloud Service
Firebase is used for storing images, diaries as well as user data & authentication
## Features
* HILT injection
* MVVM architecture
* One tap sign in

## One Tap Sign In -
It makes use of firebase for handling sign ins.
Whenever a log in token is received, the user is logged in the application and then data is stored in firestore. The token allows us to fetch user data such as name, email, pfp and much more.
