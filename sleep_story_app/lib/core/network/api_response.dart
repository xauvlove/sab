import 'package:dio/dio.dart';
import 'package:equatable/equatable.dart';

/// API 统一响应模型
class ApiResponse<T> extends Equatable {
  final int code;
  final String? message;
  final T? data;
  final bool success;

  const ApiResponse({
    required this.code,
    this.message,
    this.data,
    required this.success,
  });

  factory ApiResponse.fromJson(
    Map<String, dynamic> json,
    T Function(dynamic)? fromJsonT,
  ) {
    return ApiResponse(
      code: json['code'] ?? 0,
      message: json['message'],
      data: json['data'] != null && fromJsonT != null
          ? fromJsonT(json['data'])
          : json['data'],
      success: json['success'] ?? json['code'] == 200,
    );
  }

  @override
  List<Object?> get props => [code, message, data, success];
}

/// 分页数据模型
class PageData<T> extends Equatable {
  final List<T> records;
  final int total;
  final int size;
  final int current;
  final int pages;

  const PageData({
    required this.records,
    required this.total,
    required this.size,
    required this.current,
    required this.pages,
  });

  factory PageData.fromJson(
    Map<String, dynamic> json,
    T Function(Map<String, dynamic>) fromJsonT,
  ) {
    return PageData(
      records: (json['records'] as List?)
              ?.map((e) => fromJsonT(e as Map<String, dynamic>))
              .toList() ??
          [],
      total: json['total'] ?? 0,
      size: json['size'] ?? 10,
      current: json['current'] ?? 1,
      pages: json['pages'] ?? 0,
    );
  }

  @override
  List<Object?> get props => [records, total, size, current, pages];
}

/// API 异常
class ApiException implements Exception {
  final int code;
  final String message;

  const ApiException({
    required this.code,
    required this.message,
  });

  factory ApiException.fromDioError(dynamic error) {
    if (error.response != null) {
      return ApiException(
        code: error.response.statusCode ?? 500,
        message: error.response.data?['message'] ?? '请求失败',
      );
    }

    // 网络错误
    String message = '网络连接失败';
    if (error.type == DioExceptionType.connectionTimeout) {
      message = '连接超时';
    } else if (error.type == DioExceptionType.receiveTimeout) {
      message = '接收超时';
    } else if (error.type == DioExceptionType.sendTimeout) {
      message = '发送超时';
    } else if (error.type == DioExceptionType.connectionError) {
      message = '网络连接失败';
    }

    return ApiException(code: -1, message: message);
  }

  @override
  String toString() => message;
}
