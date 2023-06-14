<template>
  <div class="contant d-flex align-center">
    <div class="box">
      <div class="search d-flex flex-row">
        <div class="left d-flex flex-row align-center">
          <div class="txid">{{ txList[txIndex].text }}</div>
          <img
            class="ml-2"
            src="@/assets/image/image_jt_t.png"
            alt=""
            @click="txClicked"
            v-if="isShowTx"
          />
          <img
            class="ml-2"
            src="@/assets/image/image_jt_d.png"
            alt=""
            @click="txClicked"
            v-else
          />

          <input v-model="inputValue" type="text" class="txInput" />
          <div class="choose" v-show="isShowTx">
            <div class="address">
              <div
                v-for="(item, index) in txList"
                :key="index"
                @click="txClick(index)"
              >
                <div class="font-14 mt-3 ml-2" style="cursor: pointer">
                  {{ item.text }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="right d-flex flex-row align-center">
          <div class="txid">{{ chainList[chainIndex].text }}</div>
          <img
            class="ml-2"
            src="@/assets/image/image_jt_t.png"
            alt=""
            @click="chainClicked"
            v-if="isShowChain"
          />
          <img
            class="ml-2"
            src="@/assets/image/image_jt_d.png"
            alt=""
            @click="chainClicked"
            v-else
          />
          <div class="chainInfo" v-if="isShowChain">
            <div
              v-for="(item, index) in chainList"
              :key="index"
              @click="chainClick(index)"
            >
              <div class="font-14 mt-3 ml-2" style="cursor: pointer">
                {{ item.text }}
              </div>
            </div>
          </div>
          <button class="btn" @click="search">Search </button>
        </div>
      </div>
      <div class="line"></div>
      <div class="analyze d-flex flex-row">
        <input v-model="analyzeValue" type="text" class="analyzeInput" placeholder="NFT Path Visualization" />
        <button class="btn" @click="Analyze">Analyze</button>
        <div class="dialog" v-if="isShowResult && analyzeinfo ">
          <img
            src="@/assets/image/image_close.png"
            @click="closeClick"
            alt=""
          />
          <div class="contants">
            <div class="hash">
              {{analyzeinfo.ibcClassId}}
            </div>
            <div class="result">Analysis results：</div>
            <div class="denomid mt-2">
              {{analyzeinfo.classPath}} 
            </div>
            <button class="copy mt-4" @click="copyBtn">Copy</button>
            <v-alert
                type="success"
                v-if="isShowAlert"
              > Copy Success</v-alert>
          </div>
        </div>
      </div>
      <div class="totalList " v-show="chainListInfo.length > 0">
           <div class="list"  v-for="(item, index1) in chainListInfo"
          :key="index1">
        <div class="title d-flex flex-row align-center mt-5">
          <img src="@/assets/image/image_jt_d.png" alt="" @click="listClicked(item,index1)" v-if="item.isShow" />
          <img  src="@/assets/image/image_jt_t.png" alt="" @click="listClicked(item,index1)" v-else>
          <div class="chainName ml-4">{{getMap(item.sourceChainId)}} </div>
          <img class="ml-3 img_jt" src="@/assets/image/image_jt.png" alt="" />
          <div class="chainName ml-4">{{getMap(item.destinationChainId)}}</div>
         
        </div>
        <div
         class="listContant d-flex flex-column"  
        >
          <div
            class="baseInfo d-flex flex-column"
            v-if="item.isShow"
          >  
            <div class=" d-flex flex-row " style=" background-color: rgb(255, 255, 255, 0.2);min-height: 260px;">
               <div class="left">
                <div class="chainName">From</div>
                <div class="chain-id mt-3 mb-3">{{ item.sourceChainId  }}</div>
                <div class="title-font-12">Channel:{{ item.sourceChannel }}</div>
				        <div class="title-font-12">NftId:{{ item.tokenID}}</div>
                <div class="title-font-12">ClassID:{{ item.sourceClassID  }}</div>
                 <div class="title-font-12">ClassPath:{{ item.sourceClassPath  }}</div>
                <div class="title-font-12">Height:{{ item.sourceHeight }}</div>
                <div class="title-font-12">Port:{{ item.sourcePort }}</div>
                <div class="title-font-12">Time:{{ item.sourceTime }}</div>
                <div class="title-font-12">Txid:{{ item.sourceTxid }}</div>
                <div class="title-font-12">Sender:{{ item.sender }}</div>
                
              </div>
              <div class="right">
                <div class="chainName">To</div>
               <div class="chain-id mt-3 mb-3">{{ item.destinationChainId }}</div>
                <div class="title-font-12">Channel:{{ item.destinationChannel }}</div>
				<div class="title-font-12">NftId:{{ item.tokenID}}</div>
                <div class="title-font-12">ClassID:{{ item.destinationClassID }}</div>
                 <div class="title-font-12">ClassPath:{{ item.destinationClassPath  }}</div>
                <div class="title-font-12">Height:{{ item.destinationHeight }}</div>
                <div class="title-font-12">Port:{{ item.destinationPort }}</div>
                <div class="title-font-12">Time:{{ item.destinationTime }}</div>
                <div class="title-font-12">Txid:{{ item.destinationTxid }}</div>
                 <div class="title-font-12">Receiver:{{ item.receiver }}</div>
              </div>
            </div>
      
                 
          </div>
        </div>

      
      </div>
      </div>
      <div class="empty" v-show="chainListInfo.length == 0"> Empty</div>
     
    </div>
  </div>
  
</template>

<script>
import {getChainListInfo,getAnalyzeInfo,getInfoByAddress} from "../../api/home";
export default {
  name: "Home",

  components: {},

  data: () => ({
    isShowAlert:false,
    share_id:0,
    listIndex:0,
    height: "",
    chainIndex: 0,
    txIndex: 0,
    isShowResult: false,
    isShowTx: false,
    isShowChain: false,
    inputValue: "",
    analyzeValue:'',
    TimeValue: 1,

    txList: [
      {
        text: "TXID",
        value: 1,
      },
      {
        text: "Address",
        value: 2,
      },
    ],

    chainList: [
      {
        text: "gon-irishub-1",
        value: 1,
      },
      {
        text: "uptick_1170-1",
        value: 2,
      },

    ],
    analyzeinfo:'',
    chainListInfo:[],
    packageInfoMap:{}
  
  }),
  mounted() {
    this.getHeight();
    window.onresize = () => {
      this.getHeight();
    };
  
  this.initMap();
  },
    filters: {
      addfilter: function(value) {
        if (value && value.length > 40) {
          return value.substr(0, 15) + "..." + value.substr(-15);
        } else {
          return value;
        }
      }
    },

  watch: {
    height() {
      this.getHeight();
    },
  },

  methods: {
    getMap(key){
      return this.packageInfoMap.get(key)
    },

    copyBtn(){
        var input = document.createElement("input");
      input.value =this.analyzeinfo.classPath
      document.body.appendChild(input);
      input.select();
      document.execCommand("copy");
      document.body.removeChild(input);
       this.isShowAlert = true;
           setTimeout(() => {
              this.isShowAlert = false;
           }, 1000);
    },
   async search(){
     let result, params ;
     if(this.txList[this.txIndex].text == 'TXID'){
         params = {
       chainId:this.chainList[this.chainIndex].text,
       txid:this.inputValue
     }
     result =  await getChainListInfo(params);
     }else{
        params = {
       chainId:this.chainList[this.chainIndex].text,
       address:this.inputValue
     }
     result =  await getInfoByAddress(params);
       
     }
   
     this.chainListInfo = result.data.data
     this.chainListInfo.forEach(element => {
      element.isShow = true
       
     });
    console.log('this.chainListInfo',this.chainListInfo);
    },
    listClicked(item,index){
      console.log('this.chainListInfo',index );
     this.chainListInfo[index].isShow = !item.isShow
      console.log('listClicked',item);
       this.$forceUpdate();
     
    },
    getHeight() {
      this.height = document.body.clientHeight - 240;
      var div = document.querySelector(".totalList");
      div.style.height = this.height + "px";
    },
    txClicked() {
      this.isShowTx = !this.isShowTx;
    },
    chainClicked() {
      this.isShowChain = !this.isShowChain;
    },
   async Analyze() {
     this.analyzeinfo = ''
       this.isShowResult = true;
    let analyzeParams = {
        ibcClassId:this.analyzeValue
      }
      let analyzeinfo =  await getAnalyzeInfo(analyzeParams);
      if(analyzeinfo){
           this.analyzeinfo = analyzeinfo.data.data
          
      }
   
      console.log('sssss',this.analyzeinfo);
    },
    closeClick() {
      this.isShowResult = false;
    },
    chainClick(index) {
      this.isShowChain = false;
      this.chainIndex = index;
    },
    txClick(index) {
      this.isShowTx = false;
      this.txIndex = index;
    },
      initMap(){
       this.packageInfoMap = new Map([
        ['gon-irishub-1', 'IRISnet'],
        ['uptick_1170-1', 'Uptick Origin']

      
    ]);

    },
  },
};
</script>

<style lang="scss" scoped>
 .empty{
    font-family: Helvetica;
   font-size: 12px;
   font-weight: normal;
  font-stretch: normal;
   letter-spacing: 1px;
  color: #ffffff;
      display: flex;
     align-self: center;
    justify-content: center;
     align-items: center;
   width: 100%;
   height: 500px;
    font-size: 20px;
 }
.title-font-12 {
  width: 340px;
  margin-top: 2px;
  font-family: Helvetica;
  font-size: 12px;
  font-weight: normal;
  font-stretch: normal;
  letter-spacing: 1px;
  color: #ffffff;
  word-break: break-all;
}
.chain-id {
  height: 10px;
  font-family: Helvetica;
  font-size: 12px;
  font-weight: bold;
  font-stretch: normal;
  line-height: 20px;
  letter-spacing: 1px;
  color: #ff99ff;
}
.chainName {
  font-family: Helvetica;
  font-size: 18px;
  font-weight: bold;
  font-stretch: normal;
  line-height: 20px;
  letter-spacing: 1px;
  color: #ffffff;
}
.line {
  margin: 25px 0;
  height: 1px;
  border: solid 1px #666666;
}
.font-14 {
  font-family: Helvetica;
  font-size: 14px;
  font-weight: normal;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #000000;
}

input[class="txInput"] {
  width: 300px;
  height: 36px;
  background-color: #ffffff;
  border-radius: 5px;
  margin-left: 34px;
   font-size: 15px;
   padding-left: 10px;
}
input[class="analyzeInput"] {
  width: 600px;
  height: 36px;
  background-color: #ffffff;
  border-radius: 5px;
  font-size: 15px;
  padding-left: 20px;
}

img {
  width: 13px;
  height: 8px;
  cursor: pointer;
}
.txid {
  font-family: Helvetica;
  font-size: 16px;
  font-weight: bold;
  font-stretch: normal;
  line-height: 52px;
  letter-spacing: 0px;
  color: #ffffff;
}
.contant {
  justify-content: center;
  margin-top: 60px;
  .box {
    width: 800px;
    height: 100vh;
    // border: 1px solid red;
    .search {
      .left {
        width: 500px;
        position: relative;
        .choose {
          width: 109px;
          height: 100px;
          background-color: #ffffff;
          border-radius: 5px;
          position: absolute;
          left: 0px;
          top: 50px;
          z-index: 200;
          .address {
            margin: 22px 15px 0 23px;
          }
        }
      }
      .right {
        width: 300px;
        position: relative;
        .btn {
          position: absolute;
          right: 0;
          width: 125px;
          height: 40px;
          background-color: #9530fd;
          border-radius: 19px;
          font-family: Helvetica;
          font-size: 15px;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #ffffff;
        }
        .chainInfo {
          position: absolute;
          left: 0;
          top: 45px;
          width: 140px;
          height: 178px;
          background-color: #ffffff;
          border-radius: 5px;
          z-index: 200;
        }
      }
    }
  }
  .analyze {
    position: relative;
    .btn {
      position: absolute;
      right: 0;
      width: 125px;
      height: 40px;
      background-color: #9530fd;
      border-radius: 19px;
      font-family: Helvetica;
      font-size: 15px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #ffffff;
    }
    .dialog {
      position: absolute;
      right: 0;
      top: 50px;
      width: 600px;
      height: 210px;
      background-image: linear-gradient(#ffffff, #ffffff),
        linear-gradient(#6f58d9, #6f58d9);
      background-blend-mode: normal, normal;
      box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
      border-radius: 5px;
      img {
        width: 30px;
        height: 31px;
        position: absolute;
        right: 8px;
        top: 10px;
      }
      .contants {
        margin: 44px 40px 0 41px;
        .copy {
          width: 75px;
          height: 31px;
          background-color: #9530fd;
          border-radius: 15px;
          font-family: Helvetica;
          font-size: 13px;
          font-weight: bold;
          font-stretch: normal;
          line-height: 15px;
          letter-spacing: 0px;
          color: #ffffff;
        }
        .hash {
          font-family: Helvetica;
          font-size: 12px;
          font-weight: normal;
          font-stretch: normal;
          line-height: 15px;
          letter-spacing: 0px;
          color: #000000;
        }
        .result {
      
          margin-top: 33px;
          font-family: Helvetica;
          font-size: 14px;
          font-weight: bold;
          font-stretch: normal;
          line-height: 15px;
          letter-spacing: 0px;
          color: #000000;
     
        }
        .denomid {
          width: 520px;
          word-break: break-all;
          font-family: Helvetica;
          font-size: 14px;
          font-weight: normal;
          font-stretch: normal;
          line-height: 15px;
          letter-spacing: 0px;
          color: #000000;
        }
      }
    }
  }
  .totalList{
          margin-top: 40px;
    width: 800px;
    overflow-y: hidden;
       .list {

    .listContant {
      margin-top: 15px;
      width: 100%;
      // border: 1px solid red;
      width: 775px;

      border-radius: 5px;
      margin-bottom: 15px;

      .baseInfo {
        width: 100%;
            
        .left {
          width: 50%;
          // border: 1px solid blue;
          padding: 22px 0 0 40px;
        }
        .right {
          width: 50%;
          // border: 1px solid green;
          padding: 22px 0 0 40px;
        }
      }
    }
    .title {
      .img_jt {
        width: 21px;
        height: 13px;
      }
    }
  }
 
  }
   .totalList:hover {
    overflow-y: auto;
  }
  .totalList::-webkit-scrollbar {
    width: 1px;

    //  background-color: #ffffff !important; /*(透明)*/
  }

  .totalList::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    background-color: #ffffff !important; /*(透明)*/
  }
  .totalList::-webkit-scrollbar-thumb {
    background-color: #ffffff !important;
  }
 
}
</style>
