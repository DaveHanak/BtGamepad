package com.umpa.btgamepad

object HidReportDescriptorDirect{
    val bytes = listOf(
        0x05, 0x01,                    // USAGE_PAGE (Generic Desktop)
        0x09, 0x05,                    // USAGE (Game Pad)
        0xa1, 0x01,                    // COLLECTION (Application)
        0xa1, 0x00,                    //   COLLECTION (Physical)
        0x05, 0x09,                    //     USAGE_PAGE (Button)
        0x19, 0x01,                    //     USAGE_MINIMUM (Button 1)
        0x29, 0x0a,                    //     USAGE_MAXIMUM (Button 10)
        0x15, 0x00,                    //     LOGICAL_MINIMUM (0)
        0x25, 0x01,                    //     LOGICAL_MAXIMUM (1)
        0x95, 0x0a,                    //     REPORT_COUNT (10)
        0x75, 0x01,                    //     REPORT_SIZE (1)
        0x81, 0x02,                    //     INPUT (Data,Var,Abs)
        0x95, 0x01,                    //     REPORT_COUNT (1)
        0x75, 0x06,                    //     REPORT_SIZE (6)
        0x81, 0x03,                    //     INPUT (Cnst,Var,Abs)
        0x05, 0x01,                    //     USAGE_PAGE (Generic Desktop)
        0x09, 0x30,                    //     USAGE (X)
        0x09, 0x31,                    //     USAGE (Y)
        0x09, 0x32,                    //     USAGE (Z)
        0x09, 0x33,                    //     USAGE (Rx)
        0x09, 0x34,                    //     USAGE (Ry)
        0x15, 0x81,                    //     LOGICAL_MINIMUM (-127)
        0x25, 0x7f,                    //     LOGICAL_MAXIMUM (127)
        0x75, 0x08,                    //     REPORT_SIZE (8)
        0x95, 0x05,                    //     REPORT_COUNT (5)
        0x81, 0x02,                    //     INPUT (Data,Var,Abs)
        0x05, 0x01,                    //     USAGE_PAGE (Generic Desktop)
        0x09, 0x39,                    //     USAGE (Hat switch)
        0x15, 0x00,                    //     LOGICAL_MINIMUM (0)
        0x25, 0x07,                    //     LOGICAL_MAXIMUM (7)
        0x75, 0x08,                    //     REPORT_SIZE (8)
        0x95, 0x01,                    //     REPORT_COUNT (1)
        0x81, 0x42,                    //     INPUT (Data,Var,Abs,Null)
        0xc0,                          //   END_COLLECTION
        0xc0                           // END_COLLECTION
    )
        .map { it.toByte() }
        .toByteArray()
}