(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3385141a"],{"0eb4":function(t,e,n){},"35f5":function(t,e,n){"use strict";n.r(e);var r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("error-content",{attrs:{code:"404",desc:"Oh~~您的页面好像飞走了~",src:t.src}})},c=[],o=n("c436"),s=n.n(o),a=n("9454"),i={name:"error_404",components:{errorContent:a["a"]},data:function(){return{src:s.a}}},u=i,l=n("2877"),f=Object(l["a"])(u,r,c,!1,null,null,null);f.options.__file="404.vue";e["default"]=f.exports},9454:function(t,e,n){"use strict";var r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"error-page"},[n("div",{staticClass:"content-con"},[n("img",{attrs:{src:t.src,alt:t.code}}),n("div",{staticClass:"text-con"},[n("h4",[t._v(t._s(t.code))]),n("h5",[t._v(t._s(t.desc))])]),n("back-btn-group",{staticClass:"back-btn-group"})],1)])},c=[],o=(n("cadf"),n("551c"),n("097d"),n("0eb4"),function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("Button",{attrs:{size:"large",type:"text"},on:{click:t.backHome}},[t._v("返回首页")]),n("Button",{attrs:{size:"large",type:"text"},on:{click:t.backPrev}},[t._v("返回上一页("+t._s(t.second)+"s)")])],1)}),s=[],a=(n("a481"),{name:"backBtnGroup",data:function(){return{second:5,timer:null}},methods:{backHome:function(){this.$router.replace({name:this.$config.homeName})},backPrev:function(){this.$router.go(-1)}},mounted:function(){var t=this;this.timer=setInterval(function(){0===t.second?t.backPrev():t.second--},1e3)},beforeDestroy:function(){clearInterval(this.timer)}}),i=a,u=n("2877"),l=Object(u["a"])(i,o,s,!1,null,null,null);l.options.__file="back-btn-group.vue";var f=l.exports,p={name:"error_content",components:{backBtnGroup:f},props:{code:String,desc:String,src:String}},d=p,v=Object(u["a"])(d,r,c,!1,null,null,null);v.options.__file="error-content.vue";e["a"]=v.exports},c436:function(t,e,n){t.exports=n.p+"img/error-404.94756dcf.svg"}}]);