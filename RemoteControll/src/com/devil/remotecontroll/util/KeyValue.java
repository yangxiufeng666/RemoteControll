package com.devil.remotecontroll.util;

public class KeyValue {
	 /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39) */
    public static final int VK_0              = 0x30;
    public static final int VK_1              = 0x31;
    public static final int VK_2              = 0x32;
    public static final int VK_3              = 0x33;
    public static final int VK_4              = 0x34;
    public static final int VK_5              = 0x35;
    public static final int VK_6              = 0x36;
    public static final int VK_7              = 0x37;
    public static final int VK_8              = 0x38;
    public static final int VK_9              = 0x39;

    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A) */
    public static final int VK_A              = 0x41;
    public static final int VK_B              = 0x42;
    public static final int VK_C              = 0x43;
    public static final int VK_D              = 0x44;
    public static final int VK_E              = 0x45;
    public static final int VK_F              = 0x46;
    public static final int VK_G              = 0x47;
    public static final int VK_H              = 0x48;
    public static final int VK_I              = 0x49;
    public static final int VK_J              = 0x4A;
    public static final int VK_K              = 0x4B;
    public static final int VK_L              = 0x4C;
    public static final int VK_M              = 0x4D;
    public static final int VK_N              = 0x4E;
    public static final int VK_O              = 0x4F;
    public static final int VK_P              = 0x50;
    public static final int VK_Q              = 0x51;
    public static final int VK_R              = 0x52;
    public static final int VK_S              = 0x53;
    public static final int VK_T              = 0x54;
    public static final int VK_U              = 0x55;
    public static final int VK_V              = 0x56;
    public static final int VK_W              = 0x57;
    public static final int VK_X              = 0x58;
    public static final int VK_Y              = 0x59;
    public static final int VK_Z              = 0x5A;
    
    public static final int VK_DELETE         = 0x7F; /* ASCII DEL */
    /**
     * Constant for the Microsoft Windows "Windows" key.
     * It is used for both the left and right version of the key.
     * @see #getKeyLocation()
     * @since 1.5
     */
    public static final int VK_WINDOWS                  = 0x020C;
    
    /** Constant for the F1 function key. */
    public static final int VK_F1             = 0x70;

    /** Constant for the F2 function key. */
    public static final int VK_F2             = 0x71;

    /** Constant for the F3 function key. */
    public static final int VK_F3             = 0x72;

    /** Constant for the F4 function key. */
    public static final int VK_F4             = 0x73;

    /** Constant for the F5 function key. */
    public static final int VK_F5             = 0x74;

    /** Constant for the F6 function key. */
    public static final int VK_F6             = 0x75;

    /** Constant for the F7 function key. */
    public static final int VK_F7             = 0x76;

    /** Constant for the F8 function key. */
    public static final int VK_F8             = 0x77;

    /** Constant for the F9 function key. */
    public static final int VK_F9             = 0x78;

    /** Constant for the F10 function key. */
    public static final int VK_F10            = 0x79;

    /** Constant for the F11 function key. */
    public static final int VK_F11            = 0x7A;

    /** Constant for the F12 function key. */
    public static final int VK_F12            = 0x7B;
    
    public static final int VK_ENTER          = '\n';
    public static final int VK_BACK_SPACE     = '\b';
    public static final int VK_TAB            = '\t';
    public static final int VK_CANCEL         = 0x03;
    public static final int VK_CLEAR          = 0x0C;
    public static final int VK_SHIFT          = 0x10;
    public static final int VK_CONTROL        = 0x11;
    public static final int VK_ALT            = 0x12;
    public static final int VK_PAUSE          = 0x13;
    public static final int VK_CAPS_LOCK      = 0x14;
    public static final int VK_ESCAPE         = 0x1B;
    public static final int VK_SPACE          = 0x20;
    public static final int VK_PAGE_UP        = 0x21;
    public static final int VK_PAGE_DOWN      = 0x22;
    public static final int VK_END            = 0x23;
    public static final int VK_HOME           = 0x24;
    /**
     * The Control key extended modifier constant.
     * @since 1.4
     */
    public static final int CTRL_DOWN_MASK = 1 << 7;
}
