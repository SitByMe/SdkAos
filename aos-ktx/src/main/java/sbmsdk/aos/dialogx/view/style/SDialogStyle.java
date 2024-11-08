package sbmsdk.aos.dialogx.view.style;

import android.content.Context;
import android.content.res.Resources;

import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.iostheme.R;

/**
 * desc   :
 * date   : 2023/7/6
 *
 * @author zoulinheng
 */
public class SDialogStyle extends DialogXStyle {
  public SDialogStyle() {
  }

  public static SDialogStyle style() {
    return new SDialogStyle();
  }

  public int layout(boolean light) {
    return light ? R.layout.layout_dialogx_ios : R.layout.layout_dialogx_ios_dark;
  }

  public int enterAnimResId() {
    return R.anim.anim_dialogx_ios_enter;
  }

  public int exitAnimResId() {
    return 0;
  }

  public int[] verticalButtonOrder() {
    return new int[]{1, 5, 3, 5, 2};
  }

  public int[] horizontalButtonOrder() {
    return new int[]{2, 5, 3, 5, 1};
  }

  public int splitWidthPx() {
    return 1;
  }

  public int splitColorRes(boolean light) {
    return light ? R.color.dialogxIOSSplitLight : R.color.dialogxIOSSplitDark;
  }

  public BlurBackgroundSetting messageDialogBlurSettings() {
    return new BlurBackgroundSetting() {
      public boolean blurBackground() {
        return true;
      }

      public int blurForwardColorRes(boolean light) {
        return light ? R.color.dialogxIOSBkgLight : R.color.dialogxIOSBkgDark;
      }

      public int blurBackgroundRoundRadiusPx() {
        return SDialogStyle.this.dip2px(15.0F);
      }
    };
  }

  private int dip2px(float dpValue) {
    float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5F);
  }

  public HorizontalButtonRes overrideHorizontalButtonRes() {
    return new HorizontalButtonRes() {
      public int overrideHorizontalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
        if (visibleButtonCount == 1) {
          return light ? R.drawable.button_dialogx_ios_bottom_light : R.drawable.button_dialogx_ios_bottom_night;
        } else {
          return light ? R.drawable.button_dialogx_ios_right_light : R.drawable.button_dialogx_ios_right_night;
        }
      }

      public int overrideHorizontalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
        return light ? R.drawable.button_dialogx_ios_left_light : R.drawable.button_dialogx_ios_left_night;
      }

      public int overrideHorizontalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
        return light ? R.drawable.button_dialogx_ios_center_light : R.drawable.button_dialogx_ios_center_night;
      }
    };
  }

  public VerticalButtonRes overrideVerticalButtonRes() {
    return new VerticalButtonRes() {
      public int overrideVerticalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
        return light ? R.drawable.button_dialogx_ios_center_light : R.drawable.button_dialogx_ios_center_night;
      }

      public int overrideVerticalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
        return light ? R.drawable.button_dialogx_ios_bottom_light : R.drawable.button_dialogx_ios_bottom_night;
      }

      public int overrideVerticalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
        return light ? R.drawable.button_dialogx_ios_center_light : R.drawable.button_dialogx_ios_center_night;
      }
    };
  }

  public WaitTipRes overrideWaitTipRes() {
    return new WaitTipRes() {
      public int overrideWaitLayout(boolean light) {
        return 0;
      }

      public int overrideRadiusPx() {
        return -1;
      }

      public boolean blurBackground() {
        return true;
      }

      public int overrideBackgroundColorRes(boolean light) {
        return light ? R.color.dialogxIOSWaitBkgDark : R.color.dialogxIOSWaitBkgLight;
      }

      public int overrideTextColorRes(boolean light) {
        return 0;
      }

      public ProgressViewInterface overrideWaitView(Context context, boolean light) {
        return (new ProgressView(context)).setLightMode(light);
      }
    };
  }

  public BottomDialogRes overrideBottomDialogRes() {
    return new BottomDialogRes() {
      public boolean touchSlide() {
        return false;
      }

      public int overrideDialogLayout(boolean light) {
        return light ? R.layout.layout_dialogx_bottom_ios : R.layout.layout_dialogx_bottom_ios_dark;
      }

      public int overrideMenuDividerDrawableRes(boolean light) {
        return light ? R.drawable.rect_dialogx_ios_menu_split_divider : R.drawable.rect_dialogx_ios_menu_split_divider_night;
      }

      public int overrideMenuDividerHeight(boolean light) {
        return 1;
      }

      public int overrideMenuTextColor(boolean light) {
        return light ? R.color.dialogxIOSBlue : R.color.dialogxIOSBlueDark;
      }

      public float overrideBottomDialogMaxHeight() {
        return 0.0F;
      }

      public int overrideMenuItemLayout(boolean light, int index, int count, boolean isContentVisibility) {
        if (light) {
          if (index == 0) {
            return isContentVisibility ? R.layout.item_dialogx_ios_bottom_menu_center_light : R.layout.item_dialogx_ios_bottom_menu_top_light;
          } else {
            return index == count - 1 ? R.layout.item_dialogx_ios_bottom_menu_bottom_light : R.layout.item_dialogx_ios_bottom_menu_center_light;
          }
        } else if (index == 0) {
          return isContentVisibility ? R.layout.item_dialogx_ios_bottom_menu_center_dark : R.layout.item_dialogx_ios_bottom_menu_top_dark;
        } else {
          return index == count - 1 ? R.layout.item_dialogx_ios_bottom_menu_bottom_dark : R.layout.item_dialogx_ios_bottom_menu_center_dark;
        }
      }

      public int overrideSelectionMenuBackgroundColor(boolean light) {
        return 0;
      }

      public boolean selectionImageTint(boolean light) {
        return true;
      }

      public int overrideSelectionImage(boolean light, boolean isSelected) {
        return 0;
      }

      public int overrideMultiSelectionImage(boolean light, boolean isSelected) {
        return 0;
      }
    };
  }

  public PopTipSettings popTipSettings() {
    return new PopTipSettings() {
      public int layout(boolean light) {
        return light ? R.layout.layout_dialogx_poptip_ios : R.layout.layout_dialogx_poptip_ios_dark;
      }

      public ALIGN align() {
        return ALIGN.TOP;
      }

      public int enterAnimResId(boolean light) {
        return R.anim.anim_dialogx_ios_top_enter;
      }

      public int exitAnimResId(boolean light) {
        return R.anim.anim_dialogx_ios_top_exit;
      }

      public boolean tintIcon() {
        return false;
      }
    };
  }

  public PopNotificationSettings popNotificationSettings() {
    return new PopNotificationSettings() {
      public int layout(boolean light) {
        return light ? R.layout.layout_dialogx_popnotification_ios : R.layout.layout_dialogx_popnotification_ios_dark;
      }

      public ALIGN align() {
        return ALIGN.TOP;
      }

      public int enterAnimResId(boolean light) {
        return R.anim.anim_dialogx_notification_enter;
      }

      public int exitAnimResId(boolean light) {
        return R.anim.anim_dialogx_notification_exit;
      }

      public boolean tintIcon() {
        return false;
      }

      public BlurBackgroundSetting blurBackgroundSettings() {
        return new BlurBackgroundSetting() {
          public boolean blurBackground() {
            return true;
          }

          public int blurForwardColorRes(boolean light) {
            return light ? R.color.dialogxIOSNotificationBkgLight : R.color.dialogxIOSNotificationBkgDark;
          }

          public int blurBackgroundRoundRadiusPx() {
            return SDialogStyle.this.dip2px(18.0F);
          }
        };
      }
    };
  }

  public PopMenuSettings popMenuSettings() {
    return new PopMenuSettings() {
      public int layout(boolean light) {
        return light ? R.layout.layout_dialogx_popmenu_ios : R.layout.layout_dialogx_popmenu_ios_dark;
      }

      public BlurBackgroundSetting blurBackgroundSettings() {
        return new BlurBackgroundSetting() {
          public boolean blurBackground() {
            return true;
          }

          public int blurForwardColorRes(boolean light) {
            return light ? R.color.dialogxIOSBkgLight : R.color.dialogxIOSBkgDark;
          }

          public int blurBackgroundRoundRadiusPx() {
            return SDialogStyle.this.dip2px(15.0F);
          }
        };
      }

      public int backgroundMaskColorRes() {
        return R.color.black20;
      }

      public int overrideMenuDividerDrawableRes(boolean light) {
        return light ? R.drawable.rect_dialogx_ios_menu_split_divider : R.drawable.rect_dialogx_ios_menu_split_divider_night;
      }

      public int overrideMenuDividerHeight(boolean light) {
        return 1;
      }

      public int overrideMenuTextColor(boolean light) {
        return 0;
      }

      public int overrideMenuItemLayoutRes(boolean light) {
        return light ? R.layout.item_dialogx_ios_popmenu_light : R.layout.item_dialogx_ios_popmenu_dark;
      }

      public int overrideMenuItemBackgroundRes(boolean light, int index, int count, boolean isContentVisibility) {
        if (light) {
          if (index == 0) {
            return R.drawable.button_dialogx_ios_top_light;
          } else {
            return index == count - 1 ? R.drawable.button_dialogx_ios_bottom_light : R.drawable.button_dialogx_ios_center_light;
          }
        } else if (index == 0) {
          return R.drawable.button_dialogx_ios_top_night;
        } else {
          return index == count - 1 ? R.drawable.button_dialogx_ios_bottom_night : R.drawable.button_dialogx_ios_center_night;
        }
      }

      public int overrideSelectionMenuBackgroundColor(boolean light) {
        return 0;
      }

      public boolean selectionImageTint(boolean light) {
        return false;
      }

      public int paddingVertical() {
        return 0;
      }
    };
  }
}