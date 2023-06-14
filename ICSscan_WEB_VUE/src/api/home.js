import axios from "axios";
const service = axios.create({
    timeout: 300000,
  });

export function getAnalyzeInfo(params) {
    return service.post('/gonapi/analyzeClass', 
        transObjToParamStr(params)
    )
}
export function getChainListInfo(params) {
    return service.post('/gonapi/getCrossChainListByTxid', 
        transObjToParamStr(params)
    )
}
export function getInfoByAddress(params) {
    return service.post('/gonapi/getCrossChainListByAddress', 
        transObjToParamStr(params)
    )
}
function transObjToParamStr(object) {
   
let arr=[]
    for (const key in object) {
        if (Object.hasOwnProperty.call(object, key)) {
            const element = object[key];
            arr.push(`${key}=${element}`);
        }
    }
    return arr.join('&');
}