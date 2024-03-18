## Swap Two Variables(easy)
Read two integers and store them in variables a and b.
Swap the values in a and b and print them out .
### Input
Two integers split by a space.
### Output
Two integers split by a space.
### Example 1
Input

    20 21
Output

    21 20
Example 2
Input

    2147483647 -2147483648
Output

    -2147483648 2147483647
---
## Polynomial Calculation(easy)
Read a floating-point number for `x` and calculate the value of the following polynomial:
$7x^4-8x^3-x^2+6x-22$
### Input
A floating-point number to read for x.
### Output
The result should be displayed with one digit after the decimal point.
### Example 1
Input

    5.5
Output

    5055.2
### Example 2
Input

    0
Output

    -22.0
---
## 身體質量指數 Body Mass Index(easy)
身體質量指數 (Body mass index, BMI) 是一種基於身高和體重，用來評估一個成年人體重是否標準的指標。BMI 的計算公式如下：

`BMI = W / H^2`

其中，`W` 是體重，單位為公斤、`H` 是身高，單位是公尺。

某甲身高 `x` 公分，體重 `y` 公斤，請計算並輸出他的 BMI。

### Input
兩個整數 `x` 和 `y`，分別代表某甲的身高（公分）和體重（公斤），其中 100 <= x <= 200，而 0 <= y <= 100。

### Output
一個單精度浮點數，代表某甲的 BMI。請輸出到小數點後六位。

## Example 1
Input

    167 77
Output

    27.609453
##Example 2
Input

    181 80
Output

    24.419281
---
## Equilateral Triangle Formula(medium)
Given the length of the side of an equilateral triangle(正三角形), Heron wants to determine the area of the equilateral triangle.

He came up with the following formula, where `a` is the length of the side and `A` is the area.

$A = \frac{\sqrt{3}}{4}a^2$

With √3 defined as `1.7320508f`, please help Heron write a program to solve this problem.

### Input
A positive integer `a` representing the length of the side of the equilateral triangle, where 0 < a <= 200.

### Output
A single precision floating point number (float) representing the area of the triangle. Please round the result to three decimal places.

### Example 1

Input

    3
Output

    3.897
Example 2

Input

    15
Output

    97.428
