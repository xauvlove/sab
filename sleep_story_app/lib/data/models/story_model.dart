import 'package:equatable/equatable.dart';

/// 故事实体
class Story extends Equatable {
  final String id;
  final String title;
  final String? description;
  final String? coverUrl;
  final String audioUrl;
  final String? author;
  final int duration; // seconds
  final String? category;
  final List<String>? tags;
  final bool isPremium;
  final int? playCount;
  final DateTime? createdAt;

  const Story({
    required this.id,
    required this.title,
    this.description,
    this.coverUrl,
    required this.audioUrl,
    this.author,
    required this.duration,
    this.category,
    this.tags,
    this.isPremium = false,
    this.playCount,
    this.createdAt,
  });

  factory Story.fromJson(Map<String, dynamic> json) {
    return Story(
      id: json['id'] ?? '',
      title: json['title'] ?? '',
      description: json['description'],
      coverUrl: json['coverUrl'],
      audioUrl: json['audioUrl'] ?? json['audio_url'] ?? '',
      author: json['author'],
      duration: json['duration'] ?? 0,
      category: json['category'],
      tags: json['tags'] != null ? List<String>.from(json['tags']) : null,
      isPremium: json['isPremium'] ?? json['is_premium'] ?? false,
      playCount: json['playCount'] ?? json['play_count'],
      createdAt: json['createdAt'] != null
          ? DateTime.parse(json['createdAt'])
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'coverUrl': coverUrl,
      'audioUrl': audioUrl,
      'author': author,
      'duration': duration,
      'category': category,
      'tags': tags,
      'isPremium': isPremium,
      'playCount': playCount,
      'createdAt': createdAt?.toIso8601String(),
    };
  }

  /// 格式化时长
  String get formattedDuration {
    final minutes = duration ~/ 60;
    final seconds = duration % 60;
    return '${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}';
  }

  @override
  List<Object?> get props => [
        id,
        title,
        description,
        coverUrl,
        audioUrl,
        author,
        duration,
        category,
        tags,
        isPremium,
        playCount,
        createdAt,
      ];
}

/// 分类实体
class Category extends Equatable {
  final String id;
  final String name;
  final String? iconUrl;
  final String? description;
  final int storyCount;

  const Category({
    required this.id,
    required this.name,
    this.iconUrl,
    this.description,
    this.storyCount = 0,
  });

  factory Category.fromJson(Map<String, dynamic> json) {
    return Category(
      id: json['id'] ?? '',
      name: json['name'] ?? '',
      iconUrl: json['iconUrl'],
      description: json['description'],
      storyCount: json['storyCount'] ?? json['story_count'] ?? 0,
    );
  }

  @override
  List<Object?> get props => [id, name, iconUrl, description, storyCount];
}

/// 首页数据
class HomeData extends Equatable {
  final List<Story> featured;
  final List<Category> categories;
  final List<Story> recommended;

  const HomeData({
    required this.featured,
    required this.categories,
    required this.recommended,
  });

  factory HomeData.fromJson(Map<String, dynamic> json) {
    return HomeData(
      featured: (json['featured'] as List?)
              ?.map((e) => Story.fromJson(e))
              .toList() ??
          [],
      categories: (json['categories'] as List?)
              ?.map((e) => Category.fromJson(e))
              .toList() ??
          [],
      recommended: (json['recommended'] as List?)
              ?.map((e) => Story.fromJson(e))
              .toList() ??
          [],
    );
  }

  @override
  List<Object?> get props => [featured, categories, recommended];
}
