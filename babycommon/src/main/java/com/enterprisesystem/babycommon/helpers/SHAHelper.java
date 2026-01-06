package com.enterprisesystem.babycommon.helpers;

import com.enterprisesystem.babycommon.utils.CommonHexUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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


    /**
     * 将字符串使用 SHA-256 算法加密并转换为十六进制字符串
     *
     * 【功能说明】
     * 对输入字符串进行 SHA-256 哈希计算，并将结果编码为十六进制字符串
     * 这是一个不可逆的单向加密函数，常用于密码加密、数据完整性校验等场景
     *
     * 【SHA-256 算法特点】
     * - 输出固定长度：无论输入多长，始终输出 64 个十六进制字符（256 位）
     * - 单向性：无法从哈希值反推出原始字符串（防暴力破解还需加盐）
     * - 雪崩效应：输入微小变化（如改变 1 个字符）会导致输出完全不同
     * - 确定性：相同输入始终产生相同输出
     * - 抗碰撞性：很难找到两个不同输入产生相同输出
     *
     * 【处理流程】
     * 1. 获取 SHA-256 MessageDigest 实例
     * 2. 将输入字符串转换为 UTF-8 编码的字节数组
     * 3. 对字节数组进行 SHA-256 哈希计算（输出 32 字节）
     * 4. 将 32 字节哈希值转换为 64 个十六进制字符
     *
     * 【使用场景】
     * - 用户密码加密存储（建议加盐 + 多次哈希）
     * - 数据完整性校验（文件哈希、API 签名）
     * - 数字签名
     * - 生成唯一标识符
     *
     * 【安全建议】
     * - 对于密码存储，建议使用 BCrypt、Argon2 或 PBKDF2 等专门算法
     * - 或者使用 SHA-256 + 盐值 + 多次迭代（如 10000 次）
     * - 不要直接使用 SHA-256 存储敏感密码（容易被彩虹表攻击）
     *
     * 【示例】
     * converByteToHexString("hello")       -> "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
     * converByteToHexString("hello!")      -> "334d016f755cd6dc58c53a86e183882f8ec14f52fb05345887c8a5edd42c87b7"
     * converByteToHexString("")            -> "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
     * converByteToHexString("password123") -> "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f"
     *
     * @param str 要加密的原始字符串（任意长度，包括空字符串）
     * @return 64 个字符的十六进制哈希字符串（小写），异常时返回空字符串 ""
     */
    public static String convertByteToHexString(String str){
        try{
            // ==================== 步骤 1：获取 SHA-256 哈希算法实例 ====================
            // MessageDigest 是 Java 提供的消息摘要算法类
            // "SHA-256" 指定使用 SHA-256 算法（输出 256 位 = 32 字节）
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            // ==================== 步骤 2：将字符串转换为字节数组 ====================
            // StandardCharsets.UTF_8 确保跨平台一致性（Windows、Linux、Mac 统一编码）
            // 例如："hello" -> [104, 101, 108, 108, 111]
            byte[] inputBytes = str.getBytes(StandardCharsets.UTF_8);

            // ==================== 步骤 3：执行 SHA-256 哈希计算 ====================
            // digest() 方法对输入字节数组进行哈希计算
            // 输出固定 32 字节（256 位）的哈希值数组
            // 例如："hello" 的 SHA-256 哈希值为 32 字节数组
            byte[] hash = messageDigest.digest(inputBytes);

            // ==================== 步骤 4：将字节数组编码为十六进制字符串 ====================
            // 使用 CommonHexUtil.encodeHexString() 方法将 32 字节转换为 64 个十六进制字符
            // 每个字节转换为 2 个十六进制字符（0-9, a-f）
            // 例如：字节 [-14, 47, 30, ...] -> "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
            String encodeStr = CommonHexUtil.encodeHexString(hash);

            return encodeStr;
        }catch (Exception var4){
            // ==================== 异常处理 ====================
            // 如果发生异常（如算法不支持、编码错误等），返回空字符串
            // 常见异常：
            // - NoSuchAlgorithmException：SHA-256 算法不可用（理论上不会发生，因为 Java 标准库支持）
            // - 其他运行时异常
            return "";
        }
    }
}
