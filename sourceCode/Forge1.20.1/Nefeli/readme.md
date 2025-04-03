# 模组介绍
## 0.1
通过监听`EntityJoinLevelEvent`实现直射附魔
通过监听`ArrowLooseEvent`实现霰弹
通过监听`PlayerTickEvent`实现速射 
吸血附魔可直接重写`doPostAttack`

## 0.2
新增 枪 子弹
子弹实体仅在客户端渲染

# 已知Bug

速射附魔为检测是否拉弓，在拉弓结束会有原版弓射箭效果

# 潜在Bug

~~在未经测试的情况下修改了Mod主类以及modid~~