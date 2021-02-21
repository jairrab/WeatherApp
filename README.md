# WeatherApp
## Architecture
The architectural goal was to design an application that is 
* scalable
* multi-layered and highly decoupled systems
* fully compliant with a team development setting, that generates low friction among members, yet with a high level of cohesion.  

For the chosen architecture, please refer to the following diagram which is taken from `Jetpack` [guide](https://developer.android.com/jetpack/guide) to app architecture.
![alt text](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)
### View Layer
* This module is responsible for handling the user experience components.
* The primary goal was to make this layer unaware of the logic of how it's being controlled (the dumber the better).
* Uses a single activity architecture and utilizing `NavController`  to handle the swapping of Fragment destinations.
### ViewModel Layer
* This module owns the logic for manipulating the `LiveData` observable states that the view components are subscribed to, and is retained during orientation changes.
* Event observers are used to implement one-time view events, such as network error toast messages. This prevents the view from repeating the action during orientation changes.
* This module owns the logic for issuing calls to the repository layer, and processing the responses for displaying to the view layer.
### Repository Layer
* This layer layer holds interfaces to the data sources through the exposed `Repository` interface. It provides intermediary access to the local and remote database. 
* The repository implementation saves web service responses into the local database. Changes to the database then trigger callbacks on active LiveData objects. Using this model, the database serves as the **single source of truth**.
* It is responsible for the transformation of responses from the weather API sources to the models in the model layer. A mapper class object is responsible for mapping the information between the remote and the model layer. 
* It is responsible for structuring data source events such as successful API calls, errors, and offline network handling through the use of Kotlin sealed classes.
### Model Layer
* The model layer holds the data models, such as the city weather information. 
* This model is responsible for the setup and access to the `Room` cached database. Access is defined through the exposed `LocalDb` interface.
* Coroutine `Flow` or continuously emitting data observables that monitors the local database is used to actively propagate information back to the reposity and view model layers.
### Remote Layer
* This module is responsible for interacting with the weather map API service using `Retrofit`.
* The API responses is not propagated directly to the view and view models. This setup decouples API responses from the rest of the application, minimizing changes to the ViewModels everytime the API model changes.
## Testing
An example of unit testings are included on the following modules
* ViewModel
** Checks the state of the `LiveData` observables during a successful, failed or error API calls.
* Repository
** Checks that the repository class receives the API call from the web service and thereafter saves it to the local database with the expected transformation, including making sure that the favorite settings is properly re-applied to the data set received from remote.
** Checks that the list of cities supplied to web service in string format is properly formatted
** Checks that the mapper function works as expected by transforming the API response into model entities
* Model
** Checks the `Room` database to make sure that saving cities and thereafter adding a new city is then collected successfully from the `Flow` data observables