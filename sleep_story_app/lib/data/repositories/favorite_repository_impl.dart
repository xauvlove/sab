import 'package:dio/dio.dart';
import '../../core/constants/api_constants.dart';
import '../../core/network/api_response.dart';
import '../../core/network/dio_client.dart';
import '../../domain/repositories/favorite_repository.dart';
import '../models/favorite_model.dart';

/// 收藏仓储实现
class FavoriteRepositoryImpl implements FavoriteRepository {
  final DioClient _client = DioClient.instance;

  @override
  Future<List<Favorite>> getFavorites({int page = 1, int size = 20}) async {
    try {
      final response = await _client.dio.get(
        ApiConstants.favorites,
        queryParameters: {'page': page, 'size': size},
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return (data['data']['records'] as List)
            .map((e) => Favorite.fromJson(e))
            .toList();
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取收藏失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<Favorite> addFavorite(String storyId) async {
    try {
      final response = await _client.dio.post(
        ApiConstants.favorites,
        data: {'storyId': storyId},
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return Favorite.fromJson(data['data']);
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '添加收藏失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<void> removeFavorite(String storyId) async {
    try {
      await _client.dio.delete('${ApiConstants.favorites}/$storyId');
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<bool> isFavorite(String storyId) async {
    try {
      final response = await _client.dio.get(
        '${ApiConstants.favorites}/$storyId/check',
      );
      final data = response.data;
      return data['code'] == 200 && data['data'] == true;
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }
}
