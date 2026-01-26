import requestHttp from '@/utils/request'

// 登录
export function login(data) {
  return requestHttp.login('/rental/user/login', data)
}

// 获取用户信息
export function getInfo() {
  return requestHttp.get('/rental/auth/info')
}

// 登出
export async function logout() {
  return await requestHttp.get("/rental/auth/logout")
}

// 获取菜单栏列表
export async function getMenuList() {
  return await requestHttp.get("/rental/auth/menuList")
}
