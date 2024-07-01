# Character Vault

Character Vault is an Android application that displays a list of characters and provides detailed information about each character. The app fetches data from the Giant Bomb API and uses Jetpack Compose for the UI. It also includes image caching for offline use.

## Features

- Display a list of characters with a search bar to filter the results.
- Show detailed information about each character including images, real name, aliases, gender, birthday, first appeared game, and a detailed description.
- Cache images for offline viewing.
- Error handling and loading indicators.

## Technologies Used

- Kotlin
- Jetpack Compose
- Retrofit
- Coil (for image loading and caching)
- Jsoup (for parsing HTML content)

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/charactervault.git
   cd charactervault

## Open the Project in Android Studio

1. Open Android Studio.
2. Click on `File` > `Open`.
3. Navigate to the location of the project and select the project folder.
4. Click `OK` to open the project.

## Add Your API Key

1. Open the `RetrofitInstance` object in your project.
2. Replace `[YOUR API KEY]` with your actual API key in the `RetrofitInstance` object.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements
- [Giant Bomb API](https://www.giantbomb.com/api/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Retrofit](https://square.github.io/retrofit/)
- [Coil](https://coil-kt.github.io/coil/)
- [Jsoup](https://jsoup.org/)

## Contact
For any inquiries or feedback, please contact [Samantha Mendez](mailto:sammierosado@gmail.com).
