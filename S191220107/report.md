## 代码原理理解

### `Scene`类的变化

从程序的入口`Scene.main()`开始理解代码，可以发现其中实例化了一个自定义的加载器类`SteganographyClassLoader`，并在`example.BubbleSorter`被默认的加载器加载之前（调用静态方法或者使用非常量静态成员之前）手动调用了`loadClass`方法。从而实现了自定义的类加载过程。

### 自定义加载器`SteganographyClassLoader`类

在加载器最关键的方法`findClass`中，可以看到类字节码的加载不是通过本地的`.class`文件，而是通过在构造函数中传入的`imageURL`所指向的在线图片，在经过`SteganographyEncoder.decodeByteArray()`将隐写术处理过的图片进行解码最终得到的`byte[]`数组。

因此可以看出，自定义加载器的核心算法就在`SteganographyClassLoader`类中。

### 隐写术图片编解码器`SteganographyEncoder`

在`SteganographyClassLoader`类中，`SteganographyEncoder`只调用了`decodeByteArray`函数，而该函数又是`decode()`函数的公共接口。。首先来看该`decode()`函数。

函数首先准备接下来核心算法要使用的变量如下：

- `int[] pixels`：调用`getRGB`，获得隐写过的图片的**所有**像素RGB信息。
- `int maxNoOfBytes`：调用`getMaxNoOfBytes()`，得到隐写字节串的最大长度。计算方法如下：
  - 首先考虑图片的尺寸`this.bi.getWidth() * this.bi.getHeight()`
  - 之后根据自定义的每个像素点的信息量（**位**数）`bitsFromColor`，总位数 / 8 之后的结果就是**字节**串的长度。
- `bytes[] result`：作为最终返回结果
- `int smallMask`：即提取隐写内容的掩码，位数为低`bitsFromColor`位~~（使用`pow`函数而不直接移位，几乎又反直觉又效率低）~~
- `int curColor`：当前所读像素点的颜色值
- `int curPix`：当前所读像素点在`pixels`数组中的索引值。
- `int charOffset`：由于一个像素点所包含的隐写信息的位数`bitsFromColor`是小于一个字节的，因此需要变量表示不同像素在同一字节中的偏移量。

接下来就是解码的核心算法部分，即通过对图片像素的解码，填满最后需要返回的字节串`result`：

- 最外层循环迭代待填充的字节。
- 内层循环中，不断增加`curPix`计数器，通过与`smallMask`进行位与运算，得到该像素点所隐写的信息，并根据`charOffset`移动到字节待填充位置。当所的信息可以填满一个字节之后，跳出循环。

`SteganographyEncoder`类中还实现了一些编码功能，即将字节串写入到图片之中。这些部分在之后需要编码图片的时候再进行展开。

## 编码隐写术图

要编码自己的隐写术图，需要完成以下两点：

1. 自定义类加载器，将本地的`.class`文件的字节码显式提取出来。
2. 将字节码传递给`SteganographyEncoder`的编码相关函数。

### 自定义类加载器

定义类`MyClassLoader`的UML图如下：
