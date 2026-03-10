import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../core/theme/app_theme.dart';
import '../../providers/player_provider.dart';
import '../home/home_screen.dart';
import '../discovery/discovery_screen.dart';
import '../community/community_screen.dart';
import '../profile/profile_screen.dart';

/// 主屏幕（底部导航）
class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  int _currentIndex = 0;

  final List<Widget> _screens = [
    const HomeScreen(),
    const DiscoveryScreen(),
    const CommunityScreen(),
    const ProfileScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: IndexedStack(
        index: _currentIndex,
        children: _screens,
      ),
      bottomNavigationBar: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          // 迷你播放器
          Consumer<PlayerProvider>(
            builder: (context, player, _) {
              if (player.status == PlayerStatus.playing ||
                  player.status == PlayerStatus.paused) {
                return _buildMiniPlayer(context, player);
              }
              return const SizedBox.shrink();
            },
          ),
          // 底部导航
          BottomNavigationBar(
            currentIndex: _currentIndex,
            onTap: (index) => setState(() => _currentIndex = index),
            items: const [
              BottomNavigationBarItem(
                icon: Icon(Icons.home_outlined),
                activeIcon: Icon(Icons.home),
                label: '首页',
              ),
              BottomNavigationBarItem(
                icon: Icon(Icons.explore_outlined),
                activeIcon: Icon(Icons.explore),
                label: '发现',
              ),
              BottomNavigationBarItem(
                icon: Icon(Icons.people_outline),
                activeIcon: Icon(Icons.people),
                label: '社区',
              ),
              BottomNavigationBarItem(
                icon: Icon(Icons.person_outline),
                activeIcon: Icon(Icons.person),
                label: '我的',
              ),
            ],
          ),
        ],
      ),
    );
  }

  /// 迷你播放器
  Widget _buildMiniPlayer(BuildContext context, PlayerProvider player) {
    return Container(
      color: AppColors.surface,
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: Row(
        children: [
          // 播放/暂停按钮
          IconButton(
            icon: Icon(
              player.status == PlayerStatus.playing
                  ? Icons.pause_circle_filled
                  : Icons.play_circle_filled,
              size: 36,
              color: AppColors.primary,
            ),
            onPressed: () {
              if (player.status == PlayerStatus.playing) {
                player.pause();
              } else {
                player.resume();
              }
            },
          ),
          // 进度条
          Expanded(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                LinearProgressIndicator(
                  value: player.progress,
                  backgroundColor: AppColors.progressInactive,
                  valueColor: AlwaysStoppedAnimation(AppColors.progressActive),
                ),
                const SizedBox(height: 4),
                Text(
                  '正在播放',
                  style: Theme.of(context).textTheme.bodySmall,
                ),
              ],
            ),
          ),
          // 停止按钮
          IconButton(
            icon: const Icon(Icons.stop, color: AppColors.textSecondary),
            onPressed: player.stop,
          ),
        ],
      ),
    );
  }
}
