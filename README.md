<h1 align="center">Tablica Korkowa</h1></br>
<p align="center">
  <a href="https://android-arsenal.com/api?level=27"><img alt="API" src="https://img.shields.io/badge/API-27%2B-brightgreen"/></a>
  <a href="https://github.com/jslowinski"><img alt="Profile" src="https://jslowinski.github.io/Website/badges/jslowinski.svg"/></a> 
</p>
<p align="center">  
A simple Private Lesson app using compose based on modern Android tech-stacks and MVVM architecture created for the purpose of the subject at university.
</p>
</br>

<p align="center">
<img src="/previews/screenshot.png"/>
</p>

## Tech stack & Open-source libraries
- Minimum SDK level 27
- [Kotlin](https://kotlinlang.org/) based + [RxJava](https://github.com/ReactiveX/RxJava) for asynchronous.
- JetPack
  - LiveData - notify domain layer data to views.
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
- [Firebase](https://firebase.google.com/docs/auth) - secure authentication systems
- [Retrofit2 & Gson](https://github.com/square/retrofit) - construct the REST APIs.
- [FastAdapter](https://github.com/mikepenz/FastAdapter) - create adapter for RecyclerViews
- [Glide](https://github.com/bumptech/glide) - loading images.
- [Timber](https://github.com/JakeWharton/timber) - logging.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components like cardView, TextInputLayout.
