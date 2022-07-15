# MiniGit

## 何これ

Gitのコマンドを一部実装したもの

- Gitの動作を理解するため
- GitのコマンドをJavaで実装してみたかった

## コマンド一覧

### 配管コマンド

- [x] `minigit hash-object`
- [x] `minigit cat-file`
- [ ] `minigit commit-tree`
- [ ] `minigit update-index`
- [ ] `minigit write-tree`
- [ ] `minigit symbolic-ref`

### 磁器コマンド

- [x] `minigit init`
- [ ] `minigit add`
- [ ] `minigit commit`
- [ ] `minigit branch`

## 参考

- https://git-scm.com/book/ja/v2
- https://koseki.hatenablog.com/entry/2014/04/22/inside-git-1
- http://keijinsonyaban.blogspot.com/2011/05/git.html?m=1

## Cloneした後どうすればいい？

1. VScodeに拡張機能`ms-vscode-remote.remote-containers`を入れる
1. `Dev Container`を立ち上げる
1. `Tasks: Run build task`を実行
1. ターミナルで`minigit`を実行
