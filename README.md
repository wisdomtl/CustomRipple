# CustomRipple
自定义安卓控件波纹效果

1.概述

通过不停绘制圆来模拟Android5.0 Ripple Effect

(1)支持多重波纹。

(2)支持波纹生命周期监听。

(3)支持调整波纹颜色、速度、加速效果、形状。
  
2.用法

TLRipple.java：绘制单个波纹 这个类被TLRipples使用

TLRipples.java：绘制多重波纹 这个类被控件使用

TLRippleButton.java展现了如何使用TLRipples

ThemeManager.java提供了统一修改波纹效果的接口

