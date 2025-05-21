# Le Monde Capping

Capping SDK allowing you to monitor multiple session on multiple device in order to limit the number
simultaneous sessions for an user on a specific service (for example a newspaper website).

## Latest Version
1.2

Versions listed in https://central.sonatype.com/artifact/fr.lemonde/capping/versions

## Requirements

`minSdkVersion 26`

## Setup

Add to your `build.gradle` file
```
implementation "fr.lemonde:capping:1.2"
```

### Initiate SDK
```
Capping.getInstance(applicationContext).initialize([BASE_URL], [YOUR_API_KEY])
```

If you want to configure the sdk you can also add a CappingSDKConfiguration object
```
Capping.getInstance(applicationContext).initialize([BASE_URL], [YOUR_API_KEY], configuration)
```


#### User

To identify an user you will need to set an unique identifier with `setUserId(id: String)`.


## Use the SDK

You can access the CappingSDK with `Capping.getInstance(context)`.

The component responsible for displaying the capping UI should use
```
Capping.getInstance(this).startSession()
```
and
```
Capping.getInstance(this).stopSession()
```

You can register to lock change you can attach a listener with `attachListener()`.
Don't forget to `detachListener()` when you don't need it anymore or when your ui component is destroyed.

If you want to enable your user to take control of the session you can use
```
Capping.getInstance(this).continueReading()
```

## Runtime properties

### User
You can change the `userId` at runtime with method `setUserId(id: String)`.

### Mode (default value: CappingMode.READING)
You can change the `CappingMode` at runtime with method `setMode(CappingMode.READING|CappingMode.DEVICE)`.

### Tolerance (optional, default value: 0)
Tolerance is a number representing the number of simultaneous session in the day before receiving
a block event.
If you need to change the tolerance you can use `setTolerance(tolerance)`.
By default the capping tolerance will be 0.

### Active (optional, default value: true)
If you need to disable the capping service at runtime your can use `setActive(false)`.
By default the capping will be active.

### Auto unblock (optional, default value: true)
Indicates whether the CappingLock should be automatically unlocked when the current session switches from blocked to unblocked.
Disable it if you don't want a capped user to be automatically uncapped when its current session becomes the active one.

`capping.setAutoUnblock(<true|false>)`

### Conversion event handling
If you want to track an event you can use the provided `trackEvent` function.

Example with the conversion event:
```
cappingService.trackEvent(CappingEvent.conversion) (CappingEvent.conversion or CappingEvent.popinDisplayed)
```
N.B.: the previous `trackConversionEvent` has been deprecated and will be removed in a future update.

## License

This project is licensed under the software license of Société Éditrice Du Monde, Version 1.0 - see the
[LICENSE.txt](LICENSE.txt) file for details.