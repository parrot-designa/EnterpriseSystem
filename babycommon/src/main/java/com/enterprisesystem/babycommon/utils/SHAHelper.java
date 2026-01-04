package com.enterprisesystem.babycommon.utils;

import java.util.LinkedList;
import java.util.Random;

public class SHAHelper {

    /**
     * 生成指定长度的随机密码字符串
     *
     * 【功能说明】
     * 生成强随机密码，确保密码至少包含以下四种类型的字符各一个：
     * 1. 小写字母（a-z）
     * 2. 大写字母（A-Z）
     * 3. 数字（0-9）
     * 4. 特殊字符（_\-@&=~!#$%^*()+|[]{};:"',./<>?）
     *
     * 【算法特点】
     * - 使用洗牌算法（Shuffle）确保字符位置随机分布
     * - 保证四种类型字符至少出现一次
     * - 剩余长度随机选择任意类型字符填充
     *
     * 【使用场景】
     * - 用户注册时生成初始密码
     * - 重置密码时生成新密码
     * - 生成临时密码或验证码
     *
     * 【示例】
     * getStringRandom(8)  -> "aB3$xY7!"
     * getStringRandom(12) -> "K9#mP2$qL@8x"
     *
     * @param length 要生成的密码长度，建议至少 8 位，且必须 >= 4
     *               （因为要包含四种类型的字符各至少一个）
     * @return 随机生成的密码字符串
     */
    public static String getStringRandom(int length){
        // ==================== 定义字符集 ====================

        // 特殊字符集合（包含常见的键盘特殊字符）
        // 注意：\\ 表示一个反斜杠字符（转义）
        char[] specialCharacters = "_\\-@&=~!#$%^*()+|[]{};:\"',./<>?".toCharArray();

        // 用于存储生成的每个字符（LinkedList 便于随机位置插入）
        LinkedList<String> valueNum = new LinkedList<>();

        // 随机数生成器
        Random random = new Random();

        // ==================== 初始化必选字符类型 ====================

        // must 队列存储必须包含的四种字符类型
        // 1-小写字母, 2-大写字母, 3-数字, 4-特殊字符
        // 这样确保生成的密码至少包含每种类型一个字符
        LinkedList<Integer> must = new LinkedList<Integer>();
        must.add(1);  // 必须包含小写字母
        must.add(2);  // 必须包含大写字母
        must.add(3);  // 必须包含数字
        must.add(4);  // 必须包含特殊字符

        // ==================== 初始化位置索引 ====================

        // 存储所有可用的位置索引（0, 1, 2, ..., length-1）
        // 用于洗牌算法，随机选择字符插入的位置
        LinkedList<Integer> integers = new LinkedList<>();
        for(int i = 0; i < length; i++){
            valueNum.add(null);     // 初始化为 null，后续填充
            integers.add(i);        // 添加位置索引
        }

        // ==================== 生成随机密码 ====================

        // 循环 length 次，每次生成一个字符
        for (int i = 0; i < length; i++){
            int type = 0;  // 字符类型

            // 策略：前 4 个字符必须包含四种类型（必选）
            // 后面的字符可以是任意类型（可选）
            if(must.size() > 0){
                // 从 must 队列中随机取一个类型
                int i1 = random.nextInt(must.size());  // 随机索引 0~size-1
                type = must.get(i1);                   // 获取类型
                must.remove(i1);                       // 移除已使用的类型
            } else {
                // 必选类型用完后，随机选择 1~4 中的任意类型
                while(true){
                    if((type = random.nextInt(5)) != 0){  // random.nextInt(5) 返回 0~4
                        break;  // 只要不是 0，就使用该类型
                    }
                }
            }

            // ==================== 随机选择位置 ====================

            int index, i1;
            i1 = random.nextInt(integers.size());  // 随机选择一个位置索引
            index = integers.get(i1);              // 获取具体位置
            integers.remove(i1);                    // 移除已使用的位置

            // ==================== 根据类型生成字符 ====================

            String solo = "";  // 单个字符
            switch(type){
                case 1: // 【小写字母】a-z
                    // ASCII: 'a'=97, 'z'=122
                    // random.nextInt(26) 生成 0~25
                    // +97 得到 97~122，对应 a~z
                    solo = String.valueOf((char)(random.nextInt(26) + 97));
                    break;

                case 2: // 【大写字母】A-Z
                    // ASCII: 'A'=65, 'Z'=90
                    // random.nextInt(26) 生成 0~25
                    // +65 得到 65~90，对应 A~Z
                    solo = String.valueOf((char)(random.nextInt(26) + 65));
                    break;

                case 3: // 【数字】0-9
                    // random.nextInt(10) 生成 0~9
                    solo = String.valueOf(random.nextInt(10));
                    break;

                case 4: // 【特殊字符】
                    // 从特殊字符数组中随机选择一个
                    solo = String.valueOf(specialCharacters[random.nextInt(specialCharacters.length)]);
                    break;

                default:
                    break;
            }

            // ==================== 将字符放入随机位置 ====================

            valueNum.set(index, solo);  // 在随机位置插入字符
        }

        // ==================== 拼接成字符串 ====================

        String value = "";
        for(String s : valueNum){
            value += s;  // 将所有字符拼接成字符串
        }

        return value;
    }
}
