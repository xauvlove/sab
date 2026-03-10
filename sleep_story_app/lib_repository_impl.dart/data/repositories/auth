import 'package:dio/dio.dart';
import '../../core/constants/api_constants.dart';
import '../../core/network/api_response.dart';
import '../../core/network/dio_client.dart';
import '../../domain/repositories/auth_repository.dart';
import '../models/user_model.dart';

/// 认证仓储实现
class AuthRepositoryImpl implements AuthRepository {
  final DioClient _client = DioClient.instance;

  @override
  Future<void> sendSmsCode(String phone) async {
    try {
      await _client.dio.post(
        ApiConstants.sendSms,
        data: {'phone': phone},
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<AuthResponse> smsLogin(String phone, String code) async {
    try {
      final response = await _client.dio.post(
        ApiConstants.smsLogin,
        data: {
          'phone': phone,
          'code': code,
        },
      );

      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        final authResponse = AuthResponse.fromJson(data['data']);
        // 保存 Token
        await _client.setToken(authResponse.token);
        return authResponse;
      } else {
        throw ApiException(
          code: data['code'] ?? 500,
          message: data['message'] ?? '登录失败',
        );
      }
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<AuthResponse> login(String phone, String password) async {
    try {
      final response = await _client.dio.post(
        ApiConstants.login,
        data: {
          'phone': phone,
          'password': password,
        },
      );

      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        final authResponse = AuthResponse.fromJson(data['data']);
        // 保存 Token
        await _client.setToken(authResponse.token);
        return authResponse;
      } else {
        throw ApiException(
          code: data['code'] ?? 500,
          message: data['message'] ?? '登录失败',
        );
      }
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<AuthResponse> register(String phone, String password, {String? nickname}) async {
    try {
      final response = await _client.dio.post(
        ApiConstants.register,
        data: {
          'phone': phone,
          'password': password,
          if (nickname != null) 'nickname': nickname,
        },
      );

      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        final authResponse = AuthResponse.fromJson(data['data']);
        // 保存 Token
        await _client.setToken(authResponse.token);
        return authResponse;
      } else {
        throw ApiException(
          code: data['code'] ?? 500,
          message: data['message'] ?? '注册失败',
        );
      }
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<String> refreshToken() async {
    try {
      final response = await _client.dio.post(ApiConstants.refreshToken);
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        final token = data['data']['token'];
        await _client.setToken(token);
        return token;
      }
      throw const ApiException(code: 500, message: '刷新Token失败');
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<User> getCurrentUser() async {
    try {
      final response = await _client.dio.get(ApiConstants.me);
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return User.fromJson(data['data']);
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取用户信息失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<bool> checkPhone(String phone) async {
    try {
      final response = await _client.dio.get(
        ApiConstants.checkPhone,
        queryParameters: {'phone': phone},
      );
      final data = response.data;
      return data['code'] == 200 && data['data'] == true;
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<void> logout() async {
    await _client.clearToken();
  }
}
