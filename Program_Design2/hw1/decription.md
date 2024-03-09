# Regular Expression

正則表達式（Regular Expression）是一種用來進行字串Pattern Matching的強大工具，廣泛使用的應用包含：

1. 確認字串是否符合特定的模式。例如，我們可以用正則表達式來確認一個字串是否為電子郵件地址的格式。
2. 在一個string中尋找符合特定模式的substring。如果找到，正則表達式可以提取出這些substring，或者提供這些substring的位置。
3. 替換符合特定模式的子字串。例如，我們可以用正則表達式來將一個文本中的所有中式日期格式（如112/12/31）替換為美式日期格式（如2023/12/31）。

雖然各種程式語言均有很強的regular expression功能，例如在java中，透過`java.util.regex` package可以實作複雜的regular expression應用，但在本次作業，我們將要求：

<aside>
  📖 不能使用`java.util.regex`，實現以下的功能：
  
  1. 確認是否是迴文，是的話回答Y，不是的話回答N
  2. 確認是否包含特定字串 $str_1$，是的話回答Y，不是的話回答N
  3. 確認是否包含特定字串 $str_2$ 超過或等於n次，是的話回答Y，不是的話回答N
  4. 確認是否包含字串 $a^{m}Xb^{2m}$ , where $m\geq1$, and $X$ is any string (empty is ok).
     其中 $a^m$ 指的是a或A連續重覆m次，而 $b^{2m}$ 指的是b或B連續重覆 $2m$次。
  如果符合包含字串 $a^{m}Xb^{2m}$ 的話，回答Y, 不包含的話回答N
</aside>
