# AnimationMenu

* 參考https://dribbble.com/shots/1785274-Menu-Animation-for-Additional-Functions
* 使用Camera, Matrix 達到3D效果
* 增加click animation
* 沒有scroll設計，切勿放太多item

![Screenshot](https://github.com/cuber5566/AnimationMenu/blob/master/app/src/main/res/drawable/animation_menu_pic1.jpg)

# Adapter
``` java
public interface CuberMenuAdapter {

    public int getCount();

    public String getTitle(int position);

    public int getImageResource(int position);
}

```
