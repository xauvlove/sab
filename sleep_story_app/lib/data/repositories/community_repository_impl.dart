import 'package:dio/dio.dart';
import '../../core/constants/api_constants.dart';
import '../../core/network/api_response.dart';
import '../../core/network/dio_client.dart';
import '../../domain/repositories/community_repository.dart';
import '../models/community_model.dart';

/// 社区仓储实现
class CommunityRepositoryImpl implements CommunityRepository {
  final DioClient _client = DioClient.instance;

  @override
  Future<List<CommunityStory>> getFeed({int page = 1, int size = 20}) async {
    try {
      final response = await _client.dio.get(
        ApiConstants.communityFeed,
        queryParameters: {'page': page, 'size': size},
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return (data['data']['records'] as List)
            .map((e) => CommunityStory.fromJson(e))
            .toList();
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取动态失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<CommunityStory> getStoryDetail(String id) async {
    try {
      final response = await _client.dio.get(
        ApiConstants.communityDetail.replaceAll('{id}', id),
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return CommunityStory.fromJson(data['data']);
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取详情失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<void> toggleLike(String storyId) async {
    try {
      await _client.dio.post(
        ApiConstants.communityLike.replaceAll('{id}', storyId),
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<List<CommunityStory>> getUserStories(
    String userId, {
    int page = 1,
    int size = 20,
  }) async {
    try {
      final response = await _client.dio.get(
        ApiConstants.communityUserStories.replaceAll('{userId}', userId),
        queryParameters: {'page': page, 'size': size},
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return (data['data']['records'] as List)
            .map((e) => CommunityStory.fromJson(e))
            .toList();
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取用户故事失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<CommunityStory> publishStory(PublishStoryRequest request) async {
    try {
      final response = await _client.dio.post(
        ApiConstants.communityStories,
        data: request.toJson(),
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return CommunityStory.fromJson(data['data']);
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '发布失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<void> deleteStory(String storyId) async {
    try {
      await _client.dio.delete(
        ApiConstants.communityDetail.replaceAll('{id}', storyId),
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }
}
