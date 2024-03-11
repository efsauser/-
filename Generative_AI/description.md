GAI 2024 Project1 Grading Policy
Project1: 實際運用numpy, pandas, matplotlib 與 scikit-learn
依照說明使用packages，實作題目。如果對於題目有疑慮，可以先來信詢問。

TA Email: nckudm@gmail.com (IKM Lab)

Numpy
練習 1：softmax 函數
實作 Deep Learning 中常用的機率輸出函數 softmax。

若當前任務為 
 分類問題，輸入資料 
 經過神經網路計算後得到的 logits 為 
，則分類 
 的 logits 
 經過 softmax 得到的結果 
 為：
 

依照以下提示，最終 print 隨機生成的 logits 與 softmax 的結果

    # 設定總共類別
    c = 10                    
    # 模擬輸出 logits
    x = np.random.rand(c)  
    # TODO 
    
練習 2：Linear Layer + ReLU Activation
實作 Deep Learning 中線性神經網路與常用的激發函數（Activation Function）ReLU。

令當前神經網路權重（Weight）為 
，偏差值（Bias）為 
。若神經網路輸入值為 
，則輸出值 
 為：


依照以下提示，最終 print 神經網路輸入與輸出的結果

# 設定輸入維度
d_in = 10                                
# 設定輸出維度
d_out = 30                               

# 模擬神經網路輸入
x = np.ones((d_in, 1))                   
# 模擬神經網路權重
W = np.random.rand(d_out, d_in) * 10 - 5 
# 模擬神經網路偏差值
b = np.random.rand(d_out, 1) * 10 - 5  
# TODO
pandas
練習 1：數值轉換
透過台南站氣象觀測站（467410）資料集，將 2022 年 8 月將給定的日最高紫外線指數轉換為相應的紫外線強度等級（低、中、高、甚高、極高），並新增一個欄位表示紫外線強度等級，然後計算每個等級的出現次數：

低：～、中：～、高：～、甚高：～、極高：

練習 2：條件篩選
根據給定的降水量（mm）和降水時數（hour），計算出降水強度（mm/hr），並找出降水強度大於平均強度的日期及其相關資訊 (將整個 row print 出)。

(113/3/5 update) 抱歉資料部分沒有規定完善，雨量若為 T 請以 0 取代即可

matplotlib
使用與 pandas 練習相同的氣象資料。

練習 1：折線圖
請依照日期畫出氣溫以及雨量的變化，並以折線圖的方式呈現。

練習 2：雷達圖
分析風速和風向之間的關係，對於每個風向角度區間（0-90度、90-180度、180-270度、270-360度），計算相應的平均風速，並繪製成雷達圖以可視化四種風向的風速分佈情況。

scikit-learn
本練習使用 Kaggle Titanic 所提供的資料，根據鐵達尼號乘客資料預測生還者。

點選資料集分頁後，點擊 Download All 下載所有資料並解壓縮，或是只下載 train.csv。

(113/3/1 update) 關於最終預測資料，請使用 sklearn.model_selection.train_test_split 將 train.csv 分割，參數請設置成：train_size=0.8, random_state=1012，以分割後的 test_data 做最終預測結果的分析基準

分割後 test acc 預測原始分數為：0.7262，請改進到超越此分數。

練習 1：改善決策樹分類模型
請改進 sklearn.tree.DecisionTreeClassifier 模型分類效果，可以嘗試：

增加更多的輸入特徵
使用不同的前處理方法
調整超參數
練習 2：使用不同的模型
請使用不同的模型打敗使用 sklearn.tree.DecisionTreeClassifier 模型分類效果，可以嘗試課堂所提過的方法：
以下列舉只是提供參考，可以自行查訊表現更好的模型

模型	名稱
sklearn.naive_bayes	樸素貝氏分類器（Naive Bayes Classifier）
sklearn.svm	支援向量機（Support Vector Machines）
sklearn.neighbors	近鄰演算法（Nearest Neighbors）
sklearn.ensemble	集合學習（Ensemble）
(113/3/5 update) 在這邊另外提醒：嘗試使用的模型沒有限制於以上四個，不管最終訓練結果是否超過標準請都寫成報告，實驗報告佔大部份分數，小部份分數會再依據全體達標情況分配
# 練習 Hint

# 匯入填補缺失值的工具
from sklearn.impute import SimpleImputer          
# 匯入 Label Encoder
from sklearn.preprocessing import LabelEncoder    
# 匯入決策樹模型
from sklearn.tree import DecisionTreeClassifier   
# 匯入準確度計算工具
from sklearn.metrics import accuracy_score     
# 匯入 train_test_split 工具
from sklearn.model_selection import train_test_split   

df = pd.read_csv('./data/train.csv')

# 取出訓練資料需要分析的資料欄位
df_x = df[['Sex', 'Age', 'Fare']]        
# 取出訓練資料的答案
df_y = df['Survived']

# 數值型態資料前處理
# 創造 imputer 並設定填補策略
imputer = SimpleImputer(strategy='median')        
age = df_x['Age'].to_numpy().reshape(-1, 1)
# 根據資料學習需要填補的值
imputer.fit(age)                                  
# 填補缺失值
df_x['Age'] = imputer.transform(age)           

# 類別型態資料前處理
# 創造 Label Encoder
le = LabelEncoder()                               
# 給予每個類別一個數值
le.fit(df_x['Sex'])                            
# 轉換所有類別成為數值
df_x['Sex'] = le.transform(df_x['Sex'])

# 分割 train and test sets，random_state 固定為 1012
train_x, test_x, train_y, test_y = train_test_split(df_x, df_y, train_size=0.8, random_state=1012)

# 創造決策樹模型
model = DecisionTreeClassifier(random_state=1012) 
# 訓練決策樹模型
model.fit(train_x, train_y)                       

# 確認模型是否訓練成功
pred_train = model.predict(train_x)                   
# 計算準確度
train_acc = accuracy_score(train_y, pred_train)             

# 輸出準確度
print('train accuracy: {}'.format(train_acc)) 

# 確認模型是否訓練成功
pred_test = model.predict(test_x)                   
# 計算準確度
test_acc = accuracy_score(test_y, pred_test)             

# 輸出準確度
print('test accuracy: {}'.format(test_acc)) 
