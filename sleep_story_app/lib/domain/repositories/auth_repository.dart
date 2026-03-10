import '../../data/models/user_model.dart';

/// 认证仓储接口
abstract class AuthRepository {
  /// 发送短信验证码
  Future<void> sendSmsCode(String phone);

  /// 验证码登录/注册
  Future<AuthResponse> smsLogin(String phone, String code);

  /// 用户名密码登录
  Future<AuthResponse> login(String phone, String password);

  /// 用户注册
  Future<AuthResponse> register(String phone, String password, {String? nickname});

  /// 刷新 Token
  Future<String> refreshToken();

  /// 获取当前用户信息
  Future<User> getCurrentUser();

  /// 检查手机号是否已注册
  Future<bool> checkPhone(String phone);

  /// 退出登录
  Future<void> logout();
}
