import requestHttp from "@/utils/request";

export default {
    async search(start, size, data) {
        return await requestHttp.post(`rental/role/${start}/${size}`, data);
    },
    async save(data) {
        return await requestHttp.post('rental/role', data); 
    },
    async update(data) {
        return await requestHttp.put('rental/role', data);
    },
    async hasUser(id) {
        return await requestHttp.get(`rental/role/hasUser/${id}`);
    },
    async delete(ids) {
        return await requestHttp.delete(`rental/role/${ids}`);
    },
    async getPermissionTree(userId, roleId) {
        return await requestHttp.get(`rental/role/${userId}/${roleId}`);
    },
    async assignPermission(roleId, permissionIds) {
        return await requestHttp.post(`rental/role/assignPermission${roleId}/${permissionIds}`)
    }
}