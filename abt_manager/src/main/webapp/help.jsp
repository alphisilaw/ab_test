<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="common/config.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
 <head> 
  <meta charset="UTF-8" /> 
  <meta http-equiv="X-UA-Compatible" content="IE=Edge" /> 
  <title>帮助中心</title> 
  <link rel="stylesheet" href="<%=BasePath%>/assets/css/g_better.css" /> 
 </head> 
 <body class="scroll-y"> 
  <div class="g-top"> 
   <div class="top-con"> 
    <a href="/">
    <img src="<%=BasePath%>/assets/images/logo.png" /> 
       </a>
    <span>帮助中心</span> 
    <ul> 
     <li class="li-1"><a href="/">产品介绍</a></li> 
     <li><a href="http://blog.96333.com">博客</a></li> 
     <li><a href="/login.jsp">登录</a></li> 
    </ul> 
   </div> 
  </div> 
  <div class="w-1280">
  <div class="g-con"> 
   <ul class="left-nav"> 
    <li class="act" data-id="info-1"> 
     <div class="ico-1"></div>关于Betterlly</li> 
    <li data-id="info-2"  id="act_flow"> 
     <div class="ico-2"></div>使用流程</li> 
    <li data-id="info-3"> 
     <div class="ico-3"></div>常见问题</li> 
    <!--<li data-id="info-4"> 
     <div class="ico-4"></div>AB测试研究</li> 
    <li data-id="info-5"> 
     <div class="ico-5"></div>AB测试案例</li>-->
    <li data-id="info-6" id="about-us"> 
     <div class="ico-6"></div>联系我们</li> 
   </ul> 
   <div class="content"> 
    <div class="ico-info" id="info-1"> 
     <div class="tx-tit">
       关于Betterlly 
     </div> 
     <div class="tx-con"> 
      <div class="tx-1">
        Betterlly是什么？ 
      </div> 
      <div class="tx-2"> 
       <pre>Betterlly是一款简单、科学、快速的A/B测试工具 ，它将用户对产品不同版本使用的反应进行科学对比分析，达到优化
用户体验，提升转化率、增加收益的效果。
<!--<a href="#">查看介绍视频&gt;</a>-->
      </pre> 
      </div> 
      <div class="tx-1">
        Betterlly如何帮助提升转化率？ 
      </div> 
      <div class="tx-2"> 
       <pre> Betterlly主要对追踪目标进行对比分析，比如设置“立即购买”按钮为追踪目标，系统统计不同版本的“立即购买”按
钮在位置，颜色，大小等方面的不同而造成的点击次数不同，以此对比分析，给优化该按钮提供数据依据。
      </pre> 
      </div> 
      <div class="tx-1">
        Betterlly的产品特性 
      </div> 
      <div class="tx-3">
        简单 
      </div> 
      <div class="tx-4"> 
       <pre>
(1)  可视化在线编辑，无需任何技术基础，所见即所得。
(2)  多个版本同时展示，直观分析各个版本的优劣。
(3)  自定义追踪目标 ，不仅可设置点击目标，还可设置注册、下单、支付等业绩目标。
(4)  自定义实验人群，只有符合筛选条件的访客才能进入实验。
(5)  自定义流量划分，根据需要将测试人群按照一定的比例划分给各个版本，降低风险。</pre> 
      </div> 
      <div class="tx-3">
        科学 
      </div> 
      <div class="tx-4"> 
       <pre>
(1)  采用科学的数学计算方法，对海量数据进行计算，在各个版本中分析出最佳版本。
(2)  精准跟踪用户行为，不仅能精确跟踪测试用户在当前页面的行为，更能跟踪测试用户在后续页面的活跃，付费等
      业绩行为，为提升转化率，增加收益提供有效依据。
(3)  智能分析交叉用户给实验带来的影响，当多个实验同时运行相互影响时，系统会智能分析实验间的相互影响，给
      出整体最优方案。</pre> 
      </div> 
      <div class="tx-3">
        快速便捷 
      </div> 
      <div class="tx-4"> 
       <pre>
(1)  数据快速呈现，实验启动后，最快5秒呈现数据。
(2)  随时开启、暂停测试 ，控制随心。
(3)  自动收敛流量到最高转化率版本，当不同版本优劣判别时，系统自动将人群引入至最优版本，省去新旧版本切换
      的人工过程。</pre> 
      </div> 
      <div class="tx-1">
        相关名词解释 
      </div> 
      <div class="tx-2"> 
       <pre>
(1)  A/B测试：A/B 测试是一种新兴的网页优化方法，通过分离式实验和数据量化来判断优化是否有效，以达到提高
      网站转化率，优化用户体验的效果。
(2)  转化率：有目标行为的测试用户占总测试用户人数的比率。
(3)  指标：衡量网站内容对访问者的吸引程度以及网站目标的结果。
(4)  人均化：指标数值/测试人数。
(5)  常用的目标行为(供参考) 
      a)  注册：在访问网站的用户中，注册使用网站所提供的增值服务（不仅限于会员，包括邮件、试用 等）的比例。
           在Betterlly中，可将注册按钮的“点击次数”“点击人数”作为追踪目标，或将“KPI类目标”的“注册数”
           作为追踪目标。
      b)  广告点击率： 网站广告的点击率是某一个广告内容被点击的次数在网站被浏览次数中所占比例， 反映了网页
          上广告的受关注程度，常常用来衡量广告或按钮的吸引程度
      c)  支付： 用户最终支付订单的行为，是电商网站常用的核心指标， 在 Betterlly 中，可通过设定“KPI类目标”
           中的 “提交订单”“成功订单数”等目标进行分析。</pre> 
      </div> 
      <!--<div class="tx-3">
       快速便捷
      </div> 
      <div class="tx-4">
       <pre>
Betterlly的使用流程，请查看<a href="#">“使用流程”</a>,或者直接<a href="#">“查看介绍视频”</a>。</pre> 
      </div>--> 
     </div> 
    </div> 
    <div class="ico-info" id="info-2"> 
     <div class="tx-tit">
       使用流程 
     </div> 
     <div class="tx-con"> 
      <div class="tx-1"> 
       <pre>Betterlly是一款简单、科学、快速的A/B测试工具，它将用户对产品不同版本使用的反应进行科学对比分析，帮
助优化用户体验，提升转化率、增加收益。</pre> 
      </div> 
      <div class="tx-4"> 
       <pre>Betterlly操作简单，只需以下几步即可开始一个A/B测试。
第一步：新建一个项目，添加实验；
第二步：新增测试版本，进行编辑配置；  
第三步：设置您要跟踪的目标； 
第四步：筛选测试人群与划分流量； 
第五步：拷贝项目代码到您的网页； 
第六步：启动测试，查看数据结果。</pre> 
      </div> 
      <div class="tx-1">
        详细操作步骤说明： 
      </div> 
      <div class="tx-3">
        第一步：新建一个项目，添加实验 
      </div> 
      <div class="tx-4"> 
       <pre>(1)  登陆Betterlly系统，左侧点击“添加项目”按钮，填写项目名称、描述，点击“添加”即可。
</pre> 
       <img src="<%=BasePath%>/assets/images/flow-1.png" /> 
      </div> 
      <div class="tx-4"> 
       <pre>(2)   添加项目成功后，可在左侧看见新增的项目，接下来便可在项目下添加实验，点击“添加实验”按钮，填写实验名称、
网址，所属业务，点击“确认”即可。
</pre> 
       <img src="<%=BasePath%>/assets/images/flow-2.png" /> 
      </div> 
      <div class="tx-3">
        第二步：新增测试版本，进行编辑配置 
      </div> 
      <div class="tx-4"> 
       <pre>添加试验成功后，点击实验操作项的“配置”按钮，进入在线编辑配置页面，在此页面首先添加测试版本，再进行
可视化的编辑。
</pre> 
       <img src="<%=BasePath%>/assets/images/flow-3.png" /> 
      </div> 
      <div class="tx-4"> 
       <pre>点击左上角的“＋”按钮，选择添加“在线编辑版本”或“URL分离版本”，添加相应的测试版本。
</pre> 
       <img src="<%=BasePath%>/assets/images/flow-4.png" /> 
      </div> 
      <div class="tx-4"> 
       <pre>(1)在线编辑版本
          指可在页面直接对元素进行修改的版本，修改项包括编辑样式、编辑URL、编辑文本、编辑HTML，编辑图片、
移动、调整大小、删除、隐藏等。选择“在线编辑版本”，页面直接生成一个新版本，原版本称为“控制版本”，新
增版本为测试版本。
<img src="<%=BasePath%>/assets/images/flow-5.png" />
      1)  编辑样式：指对页面元素的背景、文字颜色、大小、边框大小等样式进行编辑。
      2)  编辑URL：指更改元素的链接地址。
      3)  编辑文本：指编辑页面元素的文本信息。
      4)  编辑HTML：指编辑页面元素的HTML代码。
      5)  编辑图片：指页面元素为图片类型时，可更改图片的获取地址。
      6)  移动：指移动页面元素的位置。
      7)  调整大小：指调整页面元素的边框大小。
      8)  删除：指删除页面元素。
      9)  隐藏：指隐藏页面元素。

(2)URL版本
        指新开发一个不同的版本，将其作为测试版本，不可直接在线编辑。选择添加“URL分离版本”，填写名
称、URL地址，点击“添加“即可。
<img src="<%=BasePath%>/assets/images/flow-6.png" />
</pre> 
      </div> 
      <div class="tx-3">
        第三步：设置您要跟踪的目标 
      </div> 
      <div class="tx-4"> 
       <pre>添加测试版本（在线编辑版本或URL版本）后，便可设置追踪的目标。有两种方式可设置追踪的目标。
（1）在页面选择要追踪的目标，点击右键选择“管理目标“--&gt;”添加目标“，填写目标名称、目标类型，选
择是否作为“主目标“，确认即可。系统会根据设置统计此目标的点击人数或次数。
<img src="<%=BasePath%>/assets/images/flow-7.png" />

（2）点击顶部的“目标管理”按钮，在弹出的“管理目标”对话框框中点击“添加目标”。
<img src="<%=BasePath%>/assets/images/flow-8.png" />
添加目标分为“页面类目标”与“、KPI类目标”，其中页面类目标指在当前页面的点击次数、人数；KPI目标指注册
数、下单数等绩效指标；填写相应的目标管理信息后，点击“确认”即可。
<img src="<%=BasePath%>/assets/images/flow-9.png" />
</pre> 
      </div> 
      <div class="tx-3">
       第四步：筛选测试人群与划分流量
      </div> 
      <div class="tx-3">
       筛选测试人群
      </div> 
      <div class="tx-4">
       <pre>筛选测试人群，指设置只有符合筛选条件的人才能参加实验；页面顶部点击“限定人群”按钮，选择限定的类型与条
件，“确认”即可
<img src="<%=BasePath%>/assets/images/flow-10.png" /></pre>
      </div> 
      <div class="tx-3">
       划分版本流量
      </div> 
      <div class="tx-4">
       <pre>划分流量，指将实验人群按照设置的比例划分给不同的版本；点击页面顶部的“分流设置“按钮，设置划分的各个版
本的人群比例，“确认”即可。
<img src="<%=BasePath%>/assets/images/flow-11.png" />

实验配置完成后，您可以通过点击右上角的“查看配置“查看到实验的相关配置信息。
<img src="<%=BasePath%>/assets/images/flow-12.png" /></pre>
      </div> 
      <div class="tx-3">
       第五步：拷贝项目代码到您的网页
      </div> 
      <div class="tx-4">
       <pre>实验设置完成后，便可将项目代码拷贝至您的网页中， 在项目页面点击“项目代码“，或在“查看配置”页面的复
制实验代码，或在“启动实验”页面查看代码，将代码拷贝至您的网页中的&lt;head&gt;&lt;/head&gt;之间或紧挨&lt;body&gt;
之后即可。
<img src="<%=BasePath%>/assets/images/flow-13.png" />
</pre>
      </div> 
      <div class="tx-3">
       第六步：启动测试，查看数据结果
      </div> 
      <div class="tx-4">
       <pre>项目代码嵌入后，点击右上角的“启动实验“按钮即可启动实验。实验启动后，将不能再修改本实验的信息，所以
一定要确保相关设置完成且正确。
<img src="<%=BasePath%>/assets/images/flow-14.png" />

启动实验后，等待一段时间，即可查看统计数据，点击某个实验操作项的“查看数据”按钮，即可查看到数据概览，
指标详情，版本详情等信息。
<img src="<%=BasePath%>/assets/images/flow-15.png" />

在概览页面，可以查看到测试天数、测试人数、指标数、版本数，访客量等信息；可以查看到具体每个指标的统计数
据，选择人均化，还可以查看到每个指标人均化的数据。
<img src="<%=BasePath%>/assets/images/flow-16-0.png" />
<img src="<%=BasePath%>/assets/images/flow-16-1.png" />
指标详情页面可以查看到每个指标的各个版本的统计数据以及人均化数据，可按天或者按累计查看数据情况。对比各
个版本的测试人数以及提升率等。
<img src="<%=BasePath%>/assets/images/flow-17-0.png" />
<img src="<%=BasePath%>/assets/images/flow-17-1.png" />
版本详情可以按照版本查看数据。可以选择各个测试版本，查看相关的指标情况，同样可以选择查看人均化，选择
某个时间段查看。按天或累计的进行查看具体数据信息。
<img src="<%=BasePath%>/assets/images/flow-18-0.png" />
<img src="<%=BasePath%>/assets/images/flow-18-1.png" />
</pre> 
      </div> 
     </div> 
    </div> 
    <div class="ico-info" id="info-3"> 
     <div class="tx-tit">
       常见问题 
     </div> 
     <div class="tx-con"> 
      <div class="tx-1">
        什么是A/B测试？ 
      </div> 
      <div class="tx-2"> 
       <pre>A/B 测试是一种新兴的网页优化方法，通过分离式实验和数据量化来判断优化是否有效，以达到提高网站转化率，优
化用户体验的效果。
      </pre> 
      </div> 
      <div class="tx-1">
        什么样的网站才有必要做A/B测试？ 
      </div> 
      <div class="tx-2"> 
       <pre>A/B 测试的页面必须有较高的 UV （Unique Visitor，独立访客数），最好是过千，因为分流带有一定的随机性，如
果页面 UV 太小，分到每一个版本的人数就更少，结果很有可能被一些偶然因素影响。而 UV 较大时，根据大数定理，
我们得到的结果会接近于真实数据。
      </pre> 
      </div> 
      <div class="tx-1">
        Betterlly怎么发布一个A/B测试？ 
      </div> 
      <div class="tx-2"> 
       <pre>
请查看<a href="#" id="go_flow" data-id="act_flow">“使用流程”</a><!--,或者直接<a href="#">“查看介绍视频”</a>-->。</pre> 
      </div> 
      <div class="tx-1">
        使用A/B测试会不会影响网站的SEO? 
      </div> 
      <div class="tx-2"> 
       <pre>
不会影响.因为Betterlly嵌入的项目代码能保证搜索引擎爬取的页面URL不变，同时不会被分发到 A页面(控制页面)以外
的其他页面,所以不会影响.</pre> 
      </div> 
      <div class="tx-1">
        如何查看数据？ 
      </div> 
      <div class="tx-2"> 
       <pre>
实验启动后，等待一段时间，点击某个实验操作项的“查看数据”按钮，即可进入查看到相关的统计数据。数据统计
页面可查看到数据的概览，指标详情与版本详情。</pre> 
      </div> 
      <div class="tx-1">
        在编辑页面无法加载页面？ 
      </div> 
      <div class="tx-4"> 
       <pre>
（1） 检查您的URL是否正确.
（2） 检查网络是否正常</pre> 
      </div> 
      <div class="tx-1">
       某些页面加载后会跳转到登陆页面？
      </div> 
      <div class="tx-2">
       <pre>此现象是因为加载的页面需要进行登陆，请先切换到“浏览”模式，在浏览模式下进行登陆后，进入到需要编辑的页
面，再切换回“编辑”模式进行编辑操作。</pre>
      </div> 
     </div> 
    </div> 
    <!--
    <div class="ico-info" id="info-4"> 
     <div class="tx-tit">
       A/B测试研究 
     </div> 
     <div class="tx-con"> 
      <div class="tx-1">
       <a href="#">【干货】成为一个A/B测试专家的途径</a>
      </div> 
      <div class="tx-2">
       <pre>A / B测试,也称为对比测试,是让两个版本的登陆页面的相互pk测试。看看哪个版本能更好地引导访问者达到你的预设
目标,如注册或订阅。可以通过测试两个设计方式迥异的登陆页面或者尝试一些局部调整。......<a href="#">全文</a></pre>
      </div> 
     </div> 
    </div> 
    <div class="ico-info" id="info-5"> 
     <div class="tx-tit">
       A/B测试案例 
     </div> 
     <div class="tx-con"> 
      <div class="tx-1">
       <a href="#">经典A/B测试案例研究</a>
      </div> 
      <div class="tx-2">
       <pre>这里有一些如何进行A/B测试的案例研究。
Writing Decisions: Headline Tests on the Highrise Sign-Up Page&nbsp;37Signals测试他们的价格页面的标题。最
终发现， “30-Day Free Trial on All Accounts “比原来的“Start a Highrise Account. “多产生30 ％以上的
订单。......<a href="#">全文</a></pre>
      </div> 
     </div> 
    </div> -->
    <div class="ico-info" id="info-6"> 
     <div class="tx-tit">
       联系我们 
     </div> 
     <div class="tx-con"> 
      <div class="tx-1">
       <img src="<%=BasePath%>/assets/images/map.png" />
      </div> 
      <div class="tx-2">
       <pre class="fl-l">联系方式：

地址：深圳市南山区高新技术产业园北区朗山路7号中航工业南航大厦3楼&nbsp;
邮编：518057
联系电话：13537522920
意见反馈：xia.xie@zhenai.com
QQ群：311784469</pre> 
       <p class="wx"> 微信群:<br /> <img src="<%=BasePath%>/assets/images/wx.png" /> </p> 
       <pre class="address">交通指南：

乘坐101、201、226、233、237、334、36、81、B607、B786、B839、E9、M299、N5、高峰专线7号、高峰专线9号至
朗山路中下车，即可看见中航工业南航大厦。</pre> 
      </div> 
     </div> 
    </div> 
   </div> 
  </div> 
  </div>
  <script src="<%=BasePath%>/assets/js/jquery-1.9.1.min.js"></script> 
  <script type="text/javascript"> 
$(function() {
  $(".left-nav li").click(function(){
     var _id=$(this).data("id");
     $(this).siblings().removeClass("act");
     $(this).addClass("act");
     $(".ico-info").hide();
     $("#"+_id).show();
  });
  if(location.hash == '#aboutUs'){
      $("#about-us").trigger('click');
  };
  $("#go_flow").click(function(){
	  var _tmp=$(this).data("id");
	  var _info=$("#"+_tmp).data("id");
	  $(".left-nav li").removeClass("act");
	  $("#"+_tmp).addClass("act");
	  $(".ico-info").hide();
	  $("#"+_info).show();
	  document.documentElement.scrollTop=0;
  })
})
  </script>  
 </body>
</html>







