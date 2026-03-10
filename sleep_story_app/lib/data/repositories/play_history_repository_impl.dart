import 'package:dio/dio.dart';
import '../../core/constants/api_constants.dart';
import '../../core/network/dio_client.dart';
import '../models/play_history_model.dart';
import '../../domain/repositories/play_history_repository.dart';

/// 播放历史仓储实现
class PlayHistoryRepositoryImpl implements PlayHistoryRepository {
  DioClient get _dioClient => DioClient.instance;

  @override
  Future<List<PlayHistoryModel>> getPlayHistory({
    int limit = 20,
    int offset = 0,
  }) async {
    try {
      final response = await _dioClient.dio.get(
        ApiConstants.playHistory,
        queryParameters: {'limit': limit, 'offset': offset},
      );

      if (response.statusCode == 200 && response.data['code'] == 200) {
        final List<dynamic> data = response.data['data'] ?? [];
        return data.map((json) => PlayHistoryModel.fromJson(json)).toList();
      }
      return [];
    } on DioException catch (e) {
      throw _handleError(e);
    }
  }

  @override
  Future<void> recordPlayHistory({
    required String storyId,
    required int durationListened,
  }) async {
    try {
      await _dioClient.dio.post(
        ApiConstants.playHistory,
        data: {
          'storyId': storyId,
          'duration': durationListened,
        },
      );
    } on DioException catch (e) {
      throw _handleError(e);
    }
  }

  @override
  Future<void> deletePlayHistory(String historyId) async {
    try {
      await _dioClient.dio.delete(
        '${ApiConstants.playHistory}/$historyId',
      );
    } on DioException catch (e) {
      throw _handleError(e);
    }
  }

  @override
  Future<void> clearPlayHistory() async {
    try {
      await _dioClient.dio.delete(
        ApiConstants.playHistory,
      );
    } on DioException catch (e) {
      throw _handleError(e);
    }
  }

  String _handleError(DioException e) {
    switch (e.type) {
      case DioExceptionType.connectionTimeout:
      case DioExceptionType.sendTimeout:
      case DioExceptionType.receiveTimeout:
        return '网络连接超时，请检查网络';
      case DioExceptionType.connectionError:
        return '网络连接失败，请检查网络';
      case DioExceptionType.badResponse:
        final statusCode = e.response?.statusCode;
        final message = e.response?.data?['message'] ?? '请求失败';
        return message;
      default:
        return '请求失败：${e.message}';
    }
  }
}
