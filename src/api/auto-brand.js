import requestHttp from "@/utils/request";

export default {
    async delete(id) {
        return await requestHttp.delete(`rental/autoBrand/${id}`)
    },
    async save(data) {
        return await requestHttp.post('rental/autoBrand', data)
    },
    async search(start, size, data) {
        return await requestHttp.post(`rental/autoBrand/${start}/${size}`, data)
    },
    async update(data) {
        return await requestHttp.put('rental/autoBrand', data)
    }
}