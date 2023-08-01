package com.example.demo.enums;

public enum ResultCodeEnum {

    CODE_200(200, "message.success"),
    CODE_201(201, "message.unknown.error"),
    CODE_202(202, "message.invalid.parameter"),
    CODE_203(203, "message.network.error"),
    CODE_401(401, "message.no.auth"),
    CODE_403(403, "message.no.permission"),

    // 邮箱验证
    CODE_501(501, "validation.code.expired"),
    CODE_502(502, "validation.code.more"),
    // 账户管理错误码
    CODE_601001(601001, "message.add.user.error"),
    CODE_601002(601002, "message.update.user.error"),
    CODE_601003(601003, "message.password.must.contain.upper.and.lower.case.letters.numbers.and.symbols"),
    CODE_601005(601005, "message.bind.role.error"),
    CODE_601006(601006, "message.unbind.role.error"),
    CODE_601007(601007, "message.email.length.cannot.exceed.50"),
    CODE_601008(601008, "message.name.cannot.be.null"),
    CODE_601009(601009, "message.email.cannot.be.null"),
    CODE_601010(601010, "message.password.cannot.be.null"),
    CODE_601011(601011, "message.status.cannot.be.null"),
    CODE_601012(601012, "message.type.cannot.be.null"),
    CODE_601013(601013, "message.email.is.illegal"),
    CODE_601015(601015, "message.role.cannot.be.null"),
    CODE_601016(601016, "message.name.is.exist"),
    CODE_601017(601017, "message.name.is.illegal"),
    CODE_601018(601018, "message.freeze.error"),
    CODE_601019(601019, "message.unfreeze.error"),
    CODE_601020(601020, "message.the.current.email.address.has.been.bound.to.another.account.please.re-enter"),
    CODE_601021(601021, "message.delete.user.error"),
    CODE_601022(601022, "message.username.length.cannot.exceed.50"),
    CODE_601023(601023, "message.the.admin.has.a.common.account.and.cannot.change.the.account.type"),
    CODE_601024(601024, "message.the.number.of.admin.has.reached.the.upper.limit"),
    CODE_601025(601025, "message.the.number.of.common.users.has.reached.the.upper.limit"),
    CODE_601026(601026, "message.account.in.pending.status.cannot.be.modified"),
    CODE_601027(601027, "message.password.must.be.null"),
    CODE_601028(601028, "message.password.cannot.same.old"),
    CODE_601029(601029,"message.account.not.exist"),

    //自定义表头错误码
    CODE_701001(701001, "message.req.table.cannot.be.null"),

    //反馈上报错误码
    CODE_701005(701005, "message.req.page.cannot.be.null"),
    CODE_701006(701006, "message.req.tag.too.long"),
    CODE_701007(701007, "message.req.content.cannot.be.null"),

    ;

    private int code;
    private String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
