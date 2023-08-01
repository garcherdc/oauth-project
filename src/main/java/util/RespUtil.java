package util;



import com.alibaba.fastjson.JSON;
import com.example.demo.entity.RespVO;
import com.example.demo.enums.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RespUtil {

    public static RespVO getVO(ResultCodeEnum codeEnum, Object content) {
        RespVO respVO = new RespVO();
        respVO.setCode(codeEnum.getCode());
        respVO.setMessage("test");
        respVO.setData(content);
        respVO.setSuccess(ResultCodeEnum.CODE_200.equals(codeEnum));
        return respVO;
    }

    public static RespVO getVO(ResultCodeEnum codeEnum) {
        RespVO respVO = new RespVO();
        respVO.setCode(codeEnum.getCode());
        respVO.setMessage("test");
        respVO.setSuccess(ResultCodeEnum.CODE_200.equals(codeEnum));
        return respVO;
    }

    public static RespVO getVO(ResultCodeEnum codeEnum, String message) {
        RespVO respVO = new RespVO();
        respVO.setCode(codeEnum.getCode());
        respVO.setMessage("test");
        respVO.setSuccess(ResultCodeEnum.CODE_200.equals(codeEnum));
        return respVO;
    }

    public static RespVO getERROR() {
        return getVO(ResultCodeEnum.CODE_201);
    }

    public static RespVO getSUCCESS() {
        return getVO(ResultCodeEnum.CODE_200);
    }

    /**
     * 返回 content 结果
     *
     * @param content
     * @return
     */
    public static RespVO result(Object content) {
        return getVO(ResultCodeEnum.CODE_200, content);
    }
    public static void print(HttpServletResponse response, RespVO result) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSONString(result));
        writer.flush();
        writer.close();
    }
}
