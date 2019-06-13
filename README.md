# ALittleCar
懂点车系统 -- JavaWeb课程设计


**master分支为保护分支，不允许任何人push，请创建各自的dev分支，需要合并时提 `pull requests` 进行合并**

### 首次食用方法

克隆`master`分支

```
git clone https://github.com/zyd16888/ALittleCar.git
```

进入克隆好的目录中，创建自己的分支并进入

```
git checkout -b 分支名
```

然后打开idea导入项目：

![](https://data.singlelovely.cn/images/20190613145228.png)

注意成功导入后，右下角为你创建的分支名

![](https://data.singlelovely.cn/images/20190613145259.png)

然后将你的分支推送到远程并绑定，建议本地与远程分支名一样，-u为绑定

```
git push origin 本地分支名:远程分支名 -u
```

## 日常使用步骤

开发完成后，使用 `git add .` 将更改的文件写入暂存区，然后使用 `git commit -m "提交信息"` 将更改提交到本地git仓库，然后使用 `git push origin 分支名` ，将更改提交到远程仓库。

![](https://data.singlelovely.cn/images/20190613152533.png)

提交远程仓库后，可以到GitHub上提 `Pull requests` 请求将分支更改合入master主分支。

每次重新开始开发前，请将分支切换回`master`分支。使用 `git pull` 拉取远程分支的更改，然后将切换回自己的分支，使用 `git merge master` 将`master`分支的更改合入当前开发分支。


**注意：**

>因未配置代码检测过滤器，请勿将运行未通过的代码提交到GitHub端<br>
>提交前请一定确保代码在本地运行通过