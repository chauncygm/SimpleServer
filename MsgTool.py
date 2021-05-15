#encoding utf-8
#!/usr/bin/python3
import os
import io
import sys
import re
import shutil
import time

from string import Template
from pathlib import Path

#protoc参数
protoSerchPath = "../proto/"
targetProtoFile = "../proto/*.proto"
javaOutPath = "Message/src/main/java/"

#协议处理器handler所在目录
javaHandlerPath = "GameServer/src/main/java/script/handler/"
#协议定义类路径
msgDefinePath = "Message/src/main/java/msg/MsgDefine.java"

#协议文件解析正则
msgNameLineReg = r"^\s?message\s+[Req|Res[^ult]|Sync][A-Za-z]+\s*?\{?\s*$"
packageLineReg = r"^\s?option\s+java_package\s*=\s*\"[A-Za-z\.]+\"\s*;\s*$"
outClassLineReg = r"^\s?option\s+java_outer_classname\s*=\s*\"[A-Za-z]+\"\s*;\s*$"

#协议名前缀和标识
msgTypeTag = {"Sync":0,"Req":1,"Res":2}
#协议号对应关系{msgId:(javaPackage, outClass, msgName)}
MSG_DEFINE_INFO = {}

#解析协议,协议ID: ProtoId + Tag + MsgId
def analysisProto():
    protoId = startProtoID = 100
    for protoFile in os.listdir(protoSerchPath):
        msgId = 0
        packageName = outClassName = ""

        if (protoId := protoId + 1) >= 999:
            print("协议文件数过大!")
            sys.exit(0)

        with io.open(protoSerchPath + protoFile, encoding='utf-8') as f:
            idNames=[]
            for line in f.readlines():
                if (line.isspace()):continue
                line = line.strip()

                if re.match(packageLineReg, line):
                    strs = line.split("\"")
                    packageName = strs[1]

                if re.match(outClassLineReg, line):
                    strs = line.split("\"")
                    outClassName = strs[1]

                if re.match(msgNameLineReg, line):
                    msgId += 1
                    if (msgId >= 999):
                        print("协议定义数过大!")
                        sys.exit(0)
                    strs = line.split()
                    msgName = strs[1]
                    msgTag = -1
                    for key,value in msgTypeTag.items():
                        if msgName.startswith(key):msgTag = value
                    if msgTag == -1:continue
                    ID = str(protoId) + str(msgTag) + "{:0>2d}".format(msgId)
                    idNames.append((ID, msgName))

            for idName in idNames:
                MSG_DEFINE_INFO[idName[0]] = (packageName, outClassName, idName[1])


defineClass_template = Template(
'''/*
 * @AutoGenerate
 * Python script auto generate, Don't edit it.
 */
package msg;

import game.net.msg.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  gongshengjun
 * @date 	${CUR_TIME}
 */
public class MsgDefine {
    
    ${MSG_ID_LIST}
    public static Map<Integer, Message> allMsg() {
        Map<Integer, Message> map = new HashMap<>(1024);
        ${MAP_ID_MSG}return map;
    }
}
''')

#写入MsgDefine类
def writeDefine():
    MSG_ID_LIST = MAP_ID_MSG = ""
    CUR_TIME = str(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()))

    for key,value in MSG_DEFINE_INFO.items():
        name = value[0].replace('.', '_').upper() + '_' + value[2]
        MSG_ID_LIST += 'public static final int %s = %s;\n\t'%(name, key)
        MAP_ID_MSG += 'map.put(%s, new Message(%s,  "%s", "%s", "%s"));\n\t\t'%(name, name, value[0], value[1], value[2])
    p = Path(msgDefinePath)
    with io.open(p, 'w', encoding='utf-8') as define:
        define.write(defineClass_template.substitute(MSG_ID_LIST=MSG_ID_LIST, MAP_ID_MSG=MAP_ID_MSG, CUR_TIME=CUR_TIME))


defineHandler_template = Template(
'''/*
 * @AutoGenerate
 * Python script auto generate, Don't edit it.
 */
package script.handler.${PACK_NAME};

import game.script.IHandlerScript;
import io.netty.channel.ChannelHandlerContext;
import msg.MsgDefine;

/**
 * @author  gongshengjun
 * @date 	${CUR_TIME}
 */
public class ${CLASS_NAME} implements IHandlerScript {

    @Override
    public int getId() {
        return MsgDefine.${ID_NAME};
    }

    @Override
    public Object call(Object... objs) {
        return null;
    }

    @Override
    public <Msg> void doAction(ChannelHandlerContext chc, Msg msg) {

    }
}
''')

#写入协议处理脚本
def writeHandler():
    CUR_TIME = str(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()))

    for key,value in MSG_DEFINE_INFO.items():
        if len(value) == 0:continue
        if not(value[2].startswith('Req')):continue
        PACK_NAME = value[0].replace('msg.', '').replace('.', '/')
        p = Path(javaHandlerPath + PACK_NAME)
        p.mkdir(parents=True, exist_ok=True)

        PROTO_NAME = value[2]
        ID_NAME = value[0].replace('.', '_').upper() + '_' + value[2]

        CLASS_NAME = "On" + PROTO_NAME
        FILE_NAME = CLASS_NAME + ".java"
        f = Path(javaHandlerPath + PACK_NAME + '/' + FILE_NAME)
        if f.exists():continue
        print('生成协议脚本:' + str(f))
        with io.open(str(f), 'w', encoding='utf-8') as handler:
            handler.write(defineHandler_template.substitute(PACK_NAME=PACK_NAME, CLASS_NAME=CLASS_NAME, ID_NAME=ID_NAME, CUR_TIME=CUR_TIME))



if __name__ == "__main__":
    print("开始清理协议文件...")
    ret = os.system("rm -rf Message/src/main/java/msg/*")
    if ret == 0:print("清理协议完成.")
    print("开始生成协议文件...")
    ret = os.system("protoc --proto_path=" + protoSerchPath + " --java_out=" + javaOutPath + " " + targetProtoFile)
    if ret == 0:
        print("生成协议文件完成")
    else:
        print("协议错误！！！")
        sys.exit(0)

    #生成协议
    try:
        analysisProto()
        print('分析协议完成.')
        # print(MSG_DEFINE_INFO)
        writeDefine()
        print('生成协议定义文件完成.')
        writeHandler()
        print('生成协议脚本文件完成.')
    except:
        print("Unexpected error:", sys.exc_info()[0])
        raise




