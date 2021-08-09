# How to run

application runs on port `8089`

access applicaton with url `http://localhost:8089`

open `web-page.html` in the browser for a simple front-end demonstration of the project

# Context

游戏中有10个候选npc

每个玩家每天有五张票, 可对十个玩家进行投票

# Feature

1. 用户每天可以投票
2. 实时更新榜单

# ***可能实现***

- [ ] 通过http push 以及redis缓存的办法keep track on 登录时间 ? 以及统计当日上传次数
- [ ] 通过 java sse `(Sever-Sent-Events)` 来实现 client subscribe 至 server 进行 events 的 consumption
- [ ] 压测之类的?

# ***实际实现***

- [ ] 通过http push 以及redis缓存的办法keep track on 登录时间 ? 以及统计当日上传次数
- [x] 通过 java sse `(Sever-Sent-Events)` 来实现 client subscribe 至 server 进行 events 的 consumption
- [ ] 压测之类的?
- [x] 前端浏览html(外面看着难看里面更难看)

