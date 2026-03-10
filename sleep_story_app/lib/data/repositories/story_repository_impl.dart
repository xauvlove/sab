import 'package:dio/dio.dart';
import '../../core/constants/api_constants.dart';
import '../../core/network/api_response.dart';
import '../../core/network/dio_client.dart';
import '../../domain/repositories/story_repository.dart';
import '../models/story_model.dart';

/// 故事仓储实现
class StoryRepositoryImpl implements StoryRepository {
  final DioClient _client = DioClient.instance;

  @override
  Future<HomeData> getHomeData() async {
    try {
      final response = await _client.dio.get(ApiConstants.homeStories);
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return HomeData.fromJson(data['data']);
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取首页数据失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<Story> getStoryDetail(String id) async {
    try {
      final response = await _client.dio.get(
        ApiConstants.storyDetail.replaceAll('{id}', id),
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return Story.fromJson(data['data']);
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取故事详情失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<List<Category>> getCategories() async {
    try {
      final response = await _client.dio.get(ApiConstants.categories);
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return (data['data'] as List)
            .map((e) => Category.fromJson(e))
            .toList();
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取分类失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<List<Story>> getStoriesByCategory(
    String categoryId, {
    int page = 1,
    int size = 20,
  }) async {
    try {
      final response = await _client.dio.get(
        ApiConstants.stories,
        queryParameters: {
          'category': categoryId,
          'page': page,
          'size': size,
        },
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return (data['data']['records'] as List)
            .map((e) => Story.fromJson(e))
            .toList();
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取故事列表失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<List<Story>> searchStories(
    String keyword, {
    int page = 1,
    int size = 20,
  }) async {
    try {
      final response = await _client.dio.get(
        ApiConstants.search,
        queryParameters: {
          'keyword': keyword,
          'page': page,
          'size': size,
        },
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return (data['data']['records'] as List)
            .map((e) => Story.fromJson(e))
            .toList();
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '搜索失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }

  @override
  Future<List<Story>> getRecommendedStories({int size = 10}) async {
    try {
      final response = await _client.dio.get(
        ApiConstants.stories,
        queryParameters: {
          'recommended': true,
          'size': size,
        },
      );
      final data = response.data;
      if (data['code'] == 200 || data['success'] == true) {
        return (data['data']['records'] as List)
            .map((e) => Story.fromJson(e))
            .toList();
      }
      throw ApiException(
        code: data['code'] ?? 500,
        message: data['message'] ?? '获取推荐失败',
      );
    } on DioException catch (e) {
      throw ApiException.fromDioError(e);
    }
  }
}
