package com.yoozy.common.ansi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yoozy.common.FileAndPath.print;


/**
 * 样式控制：
 * 清除：\033[0m
 * 加粗：\033[1m
 * 斜体：\033[3m （并非所有终端支持）
 * 下划线：\033[4m                     取消下划线：\033[24m
 * 闪烁：\033[5m (开启闪烁效果)         关闭闪烁: \033[25m
 * 快速闪烁：\033[6m                    取消快闪：\033[26m
 * 反显：\033[7m
 * 消隐：\033[8m (隐藏当前文本，使其不可见)   显示：\033[28m (恢复显示)
 * 颜色控制:    \033[0m
 * [1:黑, 2:红, 3:绿, 4:黄, 5:蓝, 6:洋红 7:青, 8:白]
 * 前景色：\033[30m 到 \033[37m
 * 背景色：\033[40m 到 \033[47m
 * 前景色高亮（亮色）：\033[90m 到 \033[97m
 * 背景色高亮（亮色）：\033[100m 到 \033[107m
 * 颜色扩展:
 * 256色支持：
 * 使用 \033[38;5;<颜色编号>m 设置前景色，      \033[38;5;0m
 * 使用\033[48;5;<颜色编号>m 设置背景色，       \033[48;5;255m
 * 其中 <颜色编号> 是0到255之间的数字
 * RGB颜色模式：某些终端支持直接设置RGB颜色，例如:
 * 前景色 \033[38;2;<R>;<G>;<B>m，      \033[38;2;255;0;0m
 * 背景色 \033[48;2;<R>;<G>;<B>m，      \033[48;2;0;0;255m
 * 其中 <R>, <G>, <B> 分别是红色、绿色、蓝色的值，范围从0到255。
 * 其他操作:    \033[0m
 * 保存光标位置：\033[s，用于保存当前光标的位置。
 * 恢复光标位置：\033[u，恢复之前保存的光标位置。
 * 报告光标位置：\033[6n，终端会响应一个ESC序列，报告光标的行列位置
 * <p>
 * 滚动整个屏幕向上：\033[1T，将整个屏幕内容向上滚动一行。
 * 滚动整个屏幕向下：\033[1S，将整个屏幕内容向下滚动一行
 * <p>
 * 识别终端类型：\033[Z，请求终端返回其身份信息
 * <p>
 * 切换到备用缓冲区：\033[?1049h，通常用于全屏应用，可以隐藏光标并启用滚屏。
 * 切换回主缓冲区：\033[?1049l，从全屏或备用缓冲区回到正常模式。
 * @ClassName: ANSIEscapeCode
 * @Description: ANSI转义码
 */
public class AnsiStyle {
    public static void main(String[] args) {
        String str = "Hello World!";
        // simpleTraversalStyle();
        // simpleTraversalColorStyle();
        // System.out.println("\033[38;2;255;0;0m\033[48;2;0;0;255m"+str+"\033[0m");
        // System.out.println("\033[38;2;255;0;0m红色文本\033[0m");
        // System.out.println("\033[48;5;255m红色文本\033[0m");


        str = AnsiStyle.message(str).mode(SgrMode.FOREGROUND_COLOR).style(Color.BLUE).build();
        System.out.println(str);
    }

    /**
     * 遍历样式
     */
    public static void simpleTraversalStyle() {
        // "\033[32m" + message + "\033[0m"
        int i = -1;
        while (i <= 110) {
            String s = String.format("\033[%1$smHello World! (\\033[%1$sm)\033[0m", i++);
            print(s);
        }
    }

    public static void simpleTraversalColorStyle() {
        // "\033[32m" + message + "\033[0m"
        int i = -1;
        while (i <= 255) {
            String s = String.format("\033[48;5;%1$smHello World! (\\033[48;5;%1$sm)\033[0m", i++);
            print(s);
        }
    }

    private interface Styleable {
        // boolean isModeable(SgrMode mode);
        Styleable getDefault();
    }

    public enum Color implements Styleable {
        BLACK(0), RED(1), GREEN(2), YELLOW(3), BLUE(4), PURPLE(5), CYAN(6), WHITE(7);

        private final int value;

        // private final Mode[] modes = {Mode.BACKGROUND_COLOR, Mode.FOREGROUND_COLOR, Mode.FOREGROUND_COLOR_HIGH_LIGHT, Mode.BACKGROUND_COLOR_HIGH_LIGHT};

        Color(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public Color getDefault() {
            return Color.BLACK;
        }

        public static Color getColor(int value) {
            for (Color color : Color.values()) {
                if (color.getValue() == value) {
                    return color;
                }
            }
            return null;
        }

        public static Color getColor(String color) {
            try {
                return Color.valueOf(color.toUpperCase());
            } catch (IllegalArgumentException e) {
                // throw new RuntimeException(e);
                return null;
            }
        }

        // @Override
        // public boolean isModeable(SgrMode mode) {
        //     return Arrays.stream(modes).findAny().isPresent();
        // }
    }

    /**
     * SGR(Set graphics mode)
     */
    public enum SgrMode {
        CANCELSTYLE("0"),
        FOREGROUND_COLOR("3"),
        BACKGROUND_COLOR("4"),
        FOREGROUND_COLOR_HIGH_LIGHT("9"),
        BACKGROUND_COLOR_HIGH_LIGHT("10"),
        FOREGROUND_COLOR_RGB("38;2;"),
        BACKGROUND_COLOR_RGB("48;2;"),
        FOREGROUND_COLOR_256("38;5;"),
        BACKGROUND_COLOR_256("48;5;"),
        NORMAL("-1");

        private final String value;

        <T extends Styleable> SgrMode(String value) {
            this.value = value;
        }

        public static SgrMode getDefault() {return NORMAL;}

        public String getValue() {
            return value;
        }
    }

    // private static final String ANSI_PREFIX = "\\033[";
    // private static final String ANSI_SUFFIX = "m";

    static public class Style {

        private final List<Class<? extends Styleable>> styles
                = new ArrayList<>(Arrays.asList(Color.class));

        private Styleable style;

        private SgrMode mode;

        private final String message;


        private Style() {
            message = "";
        }
        private Style(String message) {
            this.message = message;
            mode = SgrMode.getDefault();
            style = new Styleable() {
                @Override
                public Styleable getDefault() {
                    return null;
                }
            };
        }

        public Style style(Styleable style) {
            this.style = style;
            return this;
        }

        public Style mode(SgrMode mode) {
            this.mode = mode;
            return this;
        }

        @Override
        public String toString() {
            return buildStyle();
        }

        public String build() {
            return buildStyle();
        }

        private String buildStyle() {
            String ansi;
            if (!buildable()) {
                ansi = message.toString();
                return ansi;
            }
            ansi = "\033[" +
                    mode.getValue() +
                    getStyle() +
                    "m" +
                    message;
            return ansi;
        }

        private boolean buildable() {
            return mode != SgrMode.getDefault();
        }

        public String getStyle() {
            String rtn = "";
            if (style instanceof Color) {
                rtn += ((Color) style).getValue();
            }
            return rtn;
        }

        public void setStyles(Styleable style) {
            this.style = style;
        }
    }



    public static Style message(String str) {
        return new Style(str);
    }
}

