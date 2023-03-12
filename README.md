# Flaggable [![](https://jitpack.io/v/com.tkhskt/flaggable.svg)](https://jitpack.io/#com.tkhskt/flaggable)

Flaggable is a library for switching Composable according to keys like Feature Flag.

## Setup

Refer to the [KSP quickstart guide](https://kotlinlang.org/docs/ksp-quickstart.html) to make KSP
available for your project.

### Installation

Repository is now **Jitpack**:

```gradle
repositories {
   maven { url "https://jitpack.io" }
}
```

Check the [latest-version](https://jitpack.io/#com.tkhskt/flaggable)

```gradle
implementation "com.tkhskt:flaggable:[latest-version]"
ksp "com.tkhskt:flaggable:[latest-version]"
```

## Usage

Annotate the target Composable Function with the `@Flaggable`.

The Flaggable annotation should be passed the key used to switch the Composable to be displayed and the name of the Composable Function to be generated as parameters.

Multiple Composable Functions to be switched should be placed in the same package.

```kotlin
private const val FLAG_KEY_RELEASE = "release"
private const val FLAG_KEY_DEBUG = "debug"
private const val COMPOSABLE_NAME_TEXT = "FlagText"

@Flaggable(FLAG_KEY_RELEASE, COMPOSABLE_NAME_TEXT)
@Composable
fun ReleaseText(text: String) {
    Text(
        modifier = Modifier.background(Color.Red), // Text with red background color
        text = text
    )
}

@Flaggable(FLAG_KEY_DEBUG, COMPOSABLE_NAME_TEXT)
@Composable
fun DebugText(text: String) {
    Text(
        modifier = Modifier.background(Color.Green), // Text with green background color
        text = text
    )
}
```

When the project is built, a Composable Function is generated as shown below.

```kotlin
@Composable
public fun FlagText(key: String, text: String): Unit {
  FlaggableFlagTextContainer.composableFunctions[key]?.compose(text)
}
```

By passing keys and parameters to the arguments of the generated Composable Function, the Composable Function to be displayed is toggled.

```kotlin
@Composable
fun SampleScreen() {
    Column {
        FlagText(key = FLAG_KEY_RELEASE, text = "release") // Composable Function with red background is executed
        FlagText(key = FLAG_KEY_DEBUG, text = "debug") // Composable Function with green background is executed
    }
}
```

## Examples

See the [sample](./sample) project.

## License

MIT
