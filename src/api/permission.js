import requestHttp from "@/utils/request";

export default { 
    async delete(id) {
        return await requestHttp.delete(`rental/permission/${id}`)
    },
    async hasChildren(id) {
        return await requestHttp.get(`rental/permission/${id}`)
    },
    async save(data) {
        return await requestHttp.post('rental/permission', data)
    },
    async selectTree() {
        return await requestHttp.get('rental/permission/tree')
    },
    async list() {
        return await requestHttp.get('rental/permission')
    },
    async update(data) {
        return await requestHttp.put('rental/permission', data)
    },
}