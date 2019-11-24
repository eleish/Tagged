Tagged
=====   

[![](https://jitpack.io/v/eleish/Tagged.svg)](https://jitpack.io/#eleish/Tagged)

Tagged is a simple library built in kotlin to automate fragments tags declaration and initialization using annotation processing and code generation.    

Usage
--------

Simply annotate your fragments with @Tagged to generate a tag with the fully qualified name

```
@Tagged
class SampleFragmentOne: Fragment()
```

you can also pass in your custom tag

```
@Tagged("MyCustomTag")
class SampleFragmentTwo : Fragment()
```

rebuild your project and you will find your generated tags in tagged.generated.Tags.kt file

```
package tagged.generated

import kotlin.String

const val SAMPLE_FRAGMENT_ONE_TAG: String = "com.mohamedeleish.tagged.sample.SampleFragmentOne"

const val SAMPLE_FRAGMENT_TWO_TAG: String = "MyCustomTag"
```

Setup
--------

1 - In build.gradle (app module)
```
// 1 - Add kotlin annotation processor plugin to the top of the file
apply plugin: 'kotlin-kapt' 
   
android {
    defaultConfig {
        // 2 - Set includeCompileClasspath to false
        javaCompileOptions {
             annotationProcessorOptions {
                includeCompileClasspath false
            }
        }
     } 
       
    // 3 - Add java target compile options
    compileOptions {
        sourceCompatibility 1.8
        targetCompatibility 1.8
    }
}  
   
dependencies { 
    // 4 - Add the library dependancy
    implementation 'com.github.eleish:Tagged:1.0.0'
    kapt 'com.github.eleish:Tagged:1.0.0'
}
```

2 - In build.gradle (project) add the required repositories

```maven { url 'https://jitpack.io' }```

License
-------
Apache 2.0. See the [LICENSE](https://github.com/eleish/Tagged/blob/master/LICENSE) file for details.
