package com.example.daoqimanagement.bean;

import java.util.List;

public class ResourceDetailResponse {


    @Override
    public String toString() {
        return "ResourceDetailResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : {"title":"这里是标题","content":"<p style=\"text-align: center; background: white; line-height: 2em;\"><strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\"><\/span><\/strong><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">睡觉这件事，历来受各国医家的重视。我们知道，大脑在活动的时候会发射出脑电波，科学家们通过检测人体的脑电波，发现了睡眠的五个阶段。<\/span><\/p><p style=\"text-align: justify; background: white; line-height: 2em;\"><strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">一．睡眠的不同阶段<\/span><\/strong><\/p><p style=\"text-align: justify; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">（1）睡眠的第一阶段：<\/span><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">在第一阶段人是有意识的，比如在听十分无聊的课程、讲座、会议室，会打瞌睡、做白日梦，甚至当场睡着。这时我们进入睡眠的第一阶段，称为<\/span><strong><span style=\"font-size:19px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white\">入睡期<\/span><\/strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white\">。<\/span><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">在这个状态下，我们的身体开始放松，<\/span><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white\">脑波频率渐缓，振幅渐小，<span style=\"letter-spacing:1px\">呼吸和心跳频率开始轻微的下降。<\/span><\/span><\/p><p style=\"text-align: justify; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">（2）睡眠的第二阶段：<\/span><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">在第二阶段内，大脑逐渐将其清醒时的活动停止，<\/span><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white\">开始正式睡眠，属于<\/span><strong><span style=\"font-size:19px;line-height: 150%;font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white\">浅睡阶段<\/span><\/strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white\">。<span style=\"letter-spacing:1px\">在这一阶段我们很容易被惊醒，大部分上课、开会睡觉的被叫醒的时候，基本上也是处于这个阶段。<\/span><\/span><\/p><p style=\"text-align: justify; text-indent: 32px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white\">此时脑波渐呈不规律进行，频率与振幅忽大忽小，偶尔会出现被称为\u201c睡眠锭\u201d的高频、大波幅脑波，以及被称为\u201cK结\u201d的低频、很大波幅脑波。<\/span><\/p><p style=\"text-align: justify; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">（3）睡眠的第三和第四阶段：<\/span><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">在第三和第四阶段，我们的脑电波频率降到了最低，在这两个阶段内，我们才真正睡着了，因此这些阶段也被称为<\/span><strong><span style=\"font-size:19px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">熟睡阶段<\/span><\/strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">。<\/span><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">我们进入熟睡阶段后，血压、呼吸和心跳频率降到了一天中的最低点，血管开始扩张，平时储存在我们气管中的血液也流入到内脏、肌肉中，对其进行滋养和修复。<\/span><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white\">脑波变化很大，频率只有每秒1~2周，但振幅增加较大，呈现变化缓慢的曲线。<\/span><\/p><p style=\"text-align: justify; text-indent: 32px; background: white; line-height: 2em;\"><span style=\"font-family:楷体;color:#333333;background:white\">这四个阶段的睡眠共要经过约60～90分钟，不出现眼球快速跳动现象，故统称为<\/span><strong><span style=\"font-size:19px;line-height:150%;font-family:楷体;color:#333333;background:white\">非快速眼动睡眠<\/span><\/strong><span style=\"font-family:楷体;color:#333333;background:white\">（non－rapid&nbsp;eye&nbsp;movement&nbsp;sleep，简称non－REMs）。<\/span><\/p><p style=\"text-align: left; background: white; line-height: 2em;\"><strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\"><\/span><\/strong><\/p><p style=\"text-align: justify; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">（4）睡眠的第五阶段：<\/span><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">第五阶段<\/span><strong><span style=\"font-size:19px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">快速动眼睡眠阶段<\/span><\/strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white\">（rapid&nbsp;eye&nbsp;movement&nbsp;sleep，简称REM）<span style=\"letter-spacing:1px\">。这一阶段也是我们做梦的阶段，此时脑电波频率与清醒状态一样活跃，<\/span>如果此时将其唤醒，大部分人报告说正在做梦。因此，REM就成为睡眠第五个阶段的重要特征，也成为心理学家研究做梦的重要根据。<\/span><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">科学家还发现，人们会在REM睡眠阶段消化吸收白天所学的知识和整理近期发生的事情。所以，由于睡眠不足导致记忆力下降很有可能是缺少了REM睡眠，没有足够的时间消化吸收近期获得的内容，而丢失了很多信息。<\/span><\/p><p style=\"text-align: justify; background: white; line-height: 2em;\"><strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">二．睡眠周期<\/span><\/strong><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">前面提到的五个睡眠阶段在一次完整的睡眠中不只进行一次，整个循环过程非常复杂，他们会在睡眠过程中多次重复出现，我们称之为睡眠周期。<\/span><\/p><p style=\"text-align: justify; text-indent: 34px; background: white; line-height: 2em;\"><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">在睡眠周期中，各个睡眠阶段交替出现，平均每个周期花费60-100分钟。<\/span><\/p><p style=\"text-align: center; line-height: 2em;\"><img src=\"https://www.weidue.cn:84/ueditor/php/upload/image/20210302/1614653836237624.png\" title=\"1614653836237624.png\" alt=\"123123.png\" width=\"300\" height=\"158\"/><\/p><p style=\"text-align: justify; background: white; line-height: 2em;\"><strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">三．高质量的睡眠<\/span><\/strong><\/p><p style=\"text-indent: 32px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family: &#39;微软雅黑&#39;,sans-serif\">高质量的睡眠其实就是容易熟睡，并且在这个阶段停留足够的时间。<\/span><\/p><p style=\"text-indent: 32px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family: &#39;微软雅黑&#39;,sans-serif\">熟睡是我们身体首先要尽可能满足的睡眠阶段。研究表明，<span style=\"color:#333333;background:white\">在深睡眠期，人的<a href=\"https://wenwen.sogou.com/s/?w=%E5%A4%A7%E8%84%91%E7%9A%AE%E5%B1%82&ch=ww.xqy.chain\" target=\"https://wenwen.sogou.com/z/_blank\"><span style=\"color:#333333;text-underline:none\">大脑皮层<\/span><\/a>细胞可处于充分休息状态，对稳定情绪、平衡心态、恢复精力极为重要。<\/span>如果人体缺乏熟睡阶段的睡眠，白天就会感到极度瞌睡、恶心、头痛，无法集中精力。同时，我们的免疫系统也会在次阶段开启，可以<span style=\"color:#333333;background:white\">产生大量抗体，增强抗病能力。<\/span>这就是为什么我们生病的时候会很困，并且医生会在生病的时候建议我们尽可能的多卧床休息。<\/span><\/p><p style=\"text-indent: 32px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family: &#39;微软雅黑&#39;,sans-serif\">一般来说，相对优质的睡眠各个阶段的比例大概是：第一阶段5%，第二阶段40-50%，第三、四阶段分别为12%，第五阶段25%。<\/span><\/p><p style=\"line-height: 2em;\"><span style=\"font-size: 16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">&nbsp;<\/span><\/p><p style=\"text-align: justify; background: white; line-height: 2em;\"><strong><span style=\"font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white\">四．如何提升睡眠质量？<\/span><\/strong><\/p><p style=\"margin-left: 28px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">(1)<span style=\"font:9px &#39;Times New Roman&#39;\">&nbsp;&nbsp; <\/span><\/span><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">摄取足够的阳光<\/span><\/p><p style=\"margin-left: 28px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">(2)<span style=\"font:9px &#39;Times New Roman&#39;\">&nbsp;&nbsp; <\/span><\/span><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">增加锻炼<\/span><\/p><p style=\"margin-left: 28px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">(3)<span style=\"font:9px &#39;Times New Roman&#39;\">&nbsp;&nbsp; <\/span><\/span><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">适当打盹<\/span><\/p><p style=\"margin-left: 28px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">(4)<span style=\"font:9px &#39;Times New Roman&#39;\">&nbsp;&nbsp; <\/span><\/span><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">保持良好的睡眠规律<\/span><\/p><p style=\"margin-left: 28px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">(5)<span style=\"font:9px &#39;Times New Roman&#39;\">&nbsp;&nbsp; <\/span><\/span><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">减少不良的嗜好<\/span><\/p><p style=\"margin-left: 28px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">(6)<span style=\"font:9px &#39;Times New Roman&#39;\">&nbsp;&nbsp; <\/span><\/span><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">增加非睡眠时间<\/span><\/p><p style=\"margin-left: 28px; line-height: 2em;\"><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">(7)<span style=\"font:9px &#39;Times New Roman&#39;\">&nbsp;&nbsp; <\/span><\/span><span style=\"font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">多喝水<\/span><\/p><p style=\"line-height: 2em; text-indent: 2em;\"><span style=\"font-size: 16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif\">&nbsp;......<\/span><\/p>","updatedAt":"2021-04-12 11:05:08","appendix":[{"path":"/upload/20210408/cec212052cef787f2bede80625e596cf.jpg","filename":"20210408154436.jpg"},{"path":"/upload/20210408/e6197a4af96f531a1eec1542efcb2dba.jpg","filename":"20210408154606.jpg"}]}
     * msg : ok
     */


    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    ", appendix=" + appendix +
                    '}';
        }

        /**
         * title : 这里是标题
         * content : <p style="text-align: center; background: white; line-height: 2em;"><strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white"></span></strong></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">睡觉这件事，历来受各国医家的重视。我们知道，大脑在活动的时候会发射出脑电波，科学家们通过检测人体的脑电波，发现了睡眠的五个阶段。</span></p><p style="text-align: justify; background: white; line-height: 2em;"><strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">一．睡眠的不同阶段</span></strong></p><p style="text-align: justify; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">（1）睡眠的第一阶段：</span></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">在第一阶段人是有意识的，比如在听十分无聊的课程、讲座、会议室，会打瞌睡、做白日梦，甚至当场睡着。这时我们进入睡眠的第一阶段，称为</span><strong><span style="font-size:19px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white">入睡期</span></strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white">。</span></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">在这个状态下，我们的身体开始放松，</span><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white">脑波频率渐缓，振幅渐小，<span style="letter-spacing:1px">呼吸和心跳频率开始轻微的下降。</span></span></p><p style="text-align: justify; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">（2）睡眠的第二阶段：</span></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">在第二阶段内，大脑逐渐将其清醒时的活动停止，</span><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white">开始正式睡眠，属于</span><strong><span style="font-size:19px;line-height: 150%;font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white">浅睡阶段</span></strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white">。<span style="letter-spacing:1px">在这一阶段我们很容易被惊醒，大部分上课、开会睡觉的被叫醒的时候，基本上也是处于这个阶段。</span></span></p><p style="text-align: justify; text-indent: 32px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white">此时脑波渐呈不规律进行，频率与振幅忽大忽小，偶尔会出现被称为“睡眠锭”的高频、大波幅脑波，以及被称为“K结”的低频、很大波幅脑波。</span></p><p style="text-align: justify; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">（3）睡眠的第三和第四阶段：</span></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">在第三和第四阶段，我们的脑电波频率降到了最低，在这两个阶段内，我们才真正睡着了，因此这些阶段也被称为</span><strong><span style="font-size:19px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">熟睡阶段</span></strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">。</span></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">我们进入熟睡阶段后，血压、呼吸和心跳频率降到了一天中的最低点，血管开始扩张，平时储存在我们气管中的血液也流入到内脏、肌肉中，对其进行滋养和修复。</span><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white">脑波变化很大，频率只有每秒1~2周，但振幅增加较大，呈现变化缓慢的曲线。</span></p><p style="text-align: justify; text-indent: 32px; background: white; line-height: 2em;"><span style="font-family:楷体;color:#333333;background:white">这四个阶段的睡眠共要经过约60～90分钟，不出现眼球快速跳动现象，故统称为</span><strong><span style="font-size:19px;line-height:150%;font-family:楷体;color:#333333;background:white">非快速眼动睡眠</span></strong><span style="font-family:楷体;color:#333333;background:white">（non－rapid&nbsp;eye&nbsp;movement&nbsp;sleep，简称non－REMs）。</span></p><p style="text-align: left; background: white; line-height: 2em;"><strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white"></span></strong></p><p style="text-align: justify; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">（4）睡眠的第五阶段：</span></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">第五阶段</span><strong><span style="font-size:19px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">快速动眼睡眠阶段</span></strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;background:white">（rapid&nbsp;eye&nbsp;movement&nbsp;sleep，简称REM）<span style="letter-spacing:1px">。这一阶段也是我们做梦的阶段，此时脑电波频率与清醒状态一样活跃，</span>如果此时将其唤醒，大部分人报告说正在做梦。因此，REM就成为睡眠第五个阶段的重要特征，也成为心理学家研究做梦的重要根据。</span></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">科学家还发现，人们会在REM睡眠阶段消化吸收白天所学的知识和整理近期发生的事情。所以，由于睡眠不足导致记忆力下降很有可能是缺少了REM睡眠，没有足够的时间消化吸收近期获得的内容，而丢失了很多信息。</span></p><p style="text-align: justify; background: white; line-height: 2em;"><strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">二．睡眠周期</span></strong></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">前面提到的五个睡眠阶段在一次完整的睡眠中不只进行一次，整个循环过程非常复杂，他们会在睡眠过程中多次重复出现，我们称之为睡眠周期。</span></p><p style="text-align: justify; text-indent: 34px; background: white; line-height: 2em;"><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">在睡眠周期中，各个睡眠阶段交替出现，平均每个周期花费60-100分钟。</span></p><p style="text-align: center; line-height: 2em;"><img src="https://www.weidue.cn:84/ueditor/php/upload/image/20210302/1614653836237624.png" title="1614653836237624.png" alt="123123.png" width="300" height="158"/></p><p style="text-align: justify; background: white; line-height: 2em;"><strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">三．高质量的睡眠</span></strong></p><p style="text-indent: 32px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family: &#39;微软雅黑&#39;,sans-serif">高质量的睡眠其实就是容易熟睡，并且在这个阶段停留足够的时间。</span></p><p style="text-indent: 32px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family: &#39;微软雅黑&#39;,sans-serif">熟睡是我们身体首先要尽可能满足的睡眠阶段。研究表明，<span style="color:#333333;background:white">在深睡眠期，人的<a href="https://wenwen.sogou.com/s/?w=%E5%A4%A7%E8%84%91%E7%9A%AE%E5%B1%82&ch=ww.xqy.chain" target="https://wenwen.sogou.com/z/_blank"><span style="color:#333333;text-underline:none">大脑皮层</span></a>细胞可处于充分休息状态，对稳定情绪、平衡心态、恢复精力极为重要。</span>如果人体缺乏熟睡阶段的睡眠，白天就会感到极度瞌睡、恶心、头痛，无法集中精力。同时，我们的免疫系统也会在次阶段开启，可以<span style="color:#333333;background:white">产生大量抗体，增强抗病能力。</span>这就是为什么我们生病的时候会很困，并且医生会在生病的时候建议我们尽可能的多卧床休息。</span></p><p style="text-indent: 32px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family: &#39;微软雅黑&#39;,sans-serif">一般来说，相对优质的睡眠各个阶段的比例大概是：第一阶段5%，第二阶段40-50%，第三、四阶段分别为12%，第五阶段25%。</span></p><p style="line-height: 2em;"><span style="font-size: 16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">&nbsp;</span></p><p style="text-align: justify; background: white; line-height: 2em;"><strong><span style="font-family:&#39;微软雅黑&#39;,sans-serif;color:#333333;letter-spacing:1px;background:white">四．如何提升睡眠质量？</span></strong></p><p style="margin-left: 28px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">(1)<span style="font:9px &#39;Times New Roman&#39;">&nbsp;&nbsp; </span></span><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">摄取足够的阳光</span></p><p style="margin-left: 28px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">(2)<span style="font:9px &#39;Times New Roman&#39;">&nbsp;&nbsp; </span></span><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">增加锻炼</span></p><p style="margin-left: 28px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">(3)<span style="font:9px &#39;Times New Roman&#39;">&nbsp;&nbsp; </span></span><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">适当打盹</span></p><p style="margin-left: 28px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">(4)<span style="font:9px &#39;Times New Roman&#39;">&nbsp;&nbsp; </span></span><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">保持良好的睡眠规律</span></p><p style="margin-left: 28px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">(5)<span style="font:9px &#39;Times New Roman&#39;">&nbsp;&nbsp; </span></span><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">减少不良的嗜好</span></p><p style="margin-left: 28px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">(6)<span style="font:9px &#39;Times New Roman&#39;">&nbsp;&nbsp; </span></span><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">增加非睡眠时间</span></p><p style="margin-left: 28px; line-height: 2em;"><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">(7)<span style="font:9px &#39;Times New Roman&#39;">&nbsp;&nbsp; </span></span><span style="font-size:16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">多喝水</span></p><p style="line-height: 2em; text-indent: 2em;"><span style="font-size: 16px;line-height:150%;font-family:&#39;微软雅黑&#39;,sans-serif">&nbsp;......</span></p>
         * updatedAt : 2021-04-12 11:05:08
         * appendix : [{"path":"/upload/20210408/cec212052cef787f2bede80625e596cf.jpg","filename":"20210408154436.jpg"},{"path":"/upload/20210408/e6197a4af96f531a1eec1542efcb2dba.jpg","filename":"20210408154606.jpg"}]
         */


        private String title;
        private String content;
        private String updatedAt;
        private List<AppendixBean> appendix;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<AppendixBean> getAppendix() {
            return appendix;
        }

        public void setAppendix(List<AppendixBean> appendix) {
            this.appendix = appendix;
        }

        public static class AppendixBean {
            @Override
            public String toString() {
                return "AppendixBean{" +
                        "path='" + path + '\'' +
                        ", filename='" + filename + '\'' +
                        '}';
            }

            /**
             * path : /upload/20210408/cec212052cef787f2bede80625e596cf.jpg
             * filename : 20210408154436.jpg
             */


            private String path;
            private String filename;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }
        }
    }
}
