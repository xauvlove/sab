import 'package:equatable/equatable.dart';

/// 社区故事实体
class CommunityStory extends Equatable {
  final String id;
  final String userId;
  final String? nickname;
  final String? avatarUrl;
  final String title;
  final String content;
  final int? likeCount;
  final int? listenCount;
  final bool isLiked;
  final String? authorId;
  final DateTime createdAt;

  const CommunityStory({
    required this.id,
    required this.userId,
    this.nickname,
    this.avatarUrl,
    required this.title,
    required this.content,
    this.likeCount,
    this.listenCount,
    this.isLiked = false,
    this.authorId,
    required this.createdAt,
  });

  factory CommunityStory.fromJson(Map<String, dynamic> json) {
    return CommunityStory(
      id: json['id'] ?? '',
      userId: json['userId'] ?? json['user_id'] ?? '',
      nickname: json['nickname'],
      avatarUrl: json['avatarUrl'],
      title: json['title'] ?? '',
      content: json['content'] ?? '',
      likeCount: json['likeCount'] ?? json['like_count'],
      listenCount: json['listenCount'] ?? json['listen_count'],
      isLiked: json['isLiked'] ?? json['is_liked'] ?? false,
      authorId: json['authorId'] ?? json['author_id'],
      createdAt: json['createdAt'] != null
          ? DateTime.parse(json['createdAt'])
          : DateTime.now(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'userId': userId,
      'nickname': nickname,
      'avatarUrl': avatarUrl,
      'title': title,
      'content': content,
      'likeCount': likeCount,
      'listenCount': listenCount,
      'isLiked': isLiked,
      'authorId': authorId,
      'createdAt': createdAt.toIso8601String(),
    };
  }

  CommunityStory copyWith({
    String? id,
    String? userId,
    String? nickname,
    String? avatarUrl,
    String? title,
    String? content,
    int? likeCount,
    int? listenCount,
    bool? isLiked,
    String? authorId,
    DateTime? createdAt,
  }) {
    return CommunityStory(
      id: id ?? this.id,
      userId: userId ?? this.userId,
      nickname: nickname ?? this.nickname,
      avatarUrl: avatarUrl ?? this.avatarUrl,
      title: title ?? this.title,
      content: content ?? this.content,
      likeCount: likeCount ?? this.likeCount,
      listenCount: listenCount ?? this.listenCount,
      isLiked: isLiked ?? this.isLiked,
      authorId: authorId ?? this.authorId,
      createdAt: createdAt ?? this.createdAt,
    );
  }

  @override
  List<Object?> get props => [
        id,
        userId,
        nickname,
        avatarUrl,
        title,
        content,
        likeCount,
        listenCount,
        isLiked,
        authorId,
        createdAt,
      ];
}

/// 发布故事请求
class PublishStoryRequest extends Equatable {
  final String title;
  final String content;
  final VoiceConfig? voiceConfig;

  const PublishStoryRequest({
    required this.title,
    required this.content,
    this.voiceConfig,
  });

  Map<String, dynamic> toJson() {
    return {
      'title': title,
      'content': content,
      if (voiceConfig != null) 'voiceConfig': voiceConfig!.toJson(),
    };
  }

  @override
  List<Object?> get props => [title, content, voiceConfig];
}

/// TTS 语音配置
class VoiceConfig extends Equatable {
  final double speed; // 0.5 - 2.0
  final double pitch; // 0.5 - 2.0
  final String? language; // zh-CN, en-US

  const VoiceConfig({
    this.speed = 1.0,
    this.pitch = 1.0,
    this.language = 'zh-CN',
  });

  Map<String, dynamic> toJson() {
    return {
      'speed': speed,
      'pitch': pitch,
      'language': language,
    };
  }

  @override
  List<Object?> get props => [speed, pitch, language];
}
