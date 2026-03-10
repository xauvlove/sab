import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../core/theme/app_theme.dart';
import '../../providers/auth_provider.dart';
import '../../providers/theme_provider.dart';

/// 设置页面
class SettingsScreen extends StatelessWidget {
  const SettingsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('设置'),
      ),
      body: ListView(
        children: [
          // 显示设置
          _buildSectionHeader(context, '显示设置'),
          Consumer<ThemeProvider>(
            builder: (context, themeProvider, _) {
              return _buildSwitchItem(
                context,
                icon: Icons.dark_mode,
                title: '深色模式',
                subtitle: themeProvider.isDarkMode ? '已开启' : '已关闭',
                value: themeProvider.isDarkMode,
                onChanged: (value) => themeProvider.toggleDarkMode(),
              );
            },
          ),

          // 播放设置
          _buildSectionHeader(context, '播放设置'),
          _buildSettingItem(
            context,
            icon: Icons.speed,
            title: '默认播放速度',
            subtitle: '1.0x',
            onTap: () => _showSpeedDialog(context),
          ),
          _buildSettingItem(
            context,
            icon: Icons.timer,
            title: '自动关闭时长',
            subtitle: '30分钟',
            onTap: () => _showTimerDialog(context),
          ),
          _buildSettingItem(
            context,
            icon: Icons.volume_up,
            title: '默认音量',
            subtitle: '50%',
            onTap: () {},
          ),

          // 通知设置
          _buildSectionHeader(context, '通知设置'),
          _buildSwitchItem(
            context,
            icon: Icons.notifications,
            title: '推送通知',
            value: true,
            onChanged: (value) {},
          ),
          _buildSwitchItem(
            context,
            icon: Icons.notifications_active,
            title: '每日推荐提醒',
            value: true,
            onChanged: (value) {},
          ),

          // 隐私设置
          _buildSectionHeader(context, '隐私设置'),
          _buildSwitchItem(
            context,
            icon: Icons.visibility,
            title: '显示播放历史',
            value: false,
            onChanged: (value) {},
          ),

          // 存储设置
          _buildSectionHeader(context, '存储设置'),
          _buildSettingItem(
            context,
            icon: Icons.folder,
            title: '缓存管理',
            subtitle: '125.3 MB',
            onTap: () => _showClearCacheDialog(context),
          ),
          _buildSettingItem(
            context,
            icon: Icons.download,
            title: '下载管理',
            subtitle: '0 个故事',
            onTap: () {},
          ),

          // 关于
          _buildSectionHeader(context, '关于'),
          _buildSettingItem(
            context,
            icon: Icons.info,
            title: '关于我们',
            onTap: () {},
          ),
          _buildSettingItem(
            context,
            icon: Icons.description,
            title: '用户协议',
            onTap: () {},
          ),
          _buildSettingItem(
            context,
            icon: Icons.privacy_tip,
            title: '隐私政策',
            onTap: () {},
          ),

          const SizedBox(height: 24),

          // 退出登录
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: AppColors.error,
              ),
              onPressed: () => _logout(context),
              child: const Text('退出登录'),
            ),
          ),
          const SizedBox(height: 32),
        ],
      ),
    );
  }

  Widget _buildSectionHeader(BuildContext context, String title) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 24, 16, 8),
      child: Text(
        title,
        style: Theme.of(context).textTheme.titleSmall?.copyWith(
              color: AppColors.primary,
            ),
      ),
    );
  }

  Widget _buildSettingItem(
    BuildContext context, {
    required IconData icon,
    required String title,
    String? subtitle,
    required VoidCallback onTap,
  }) {
    return ListTile(
      leading: Icon(icon, color: AppColors.textSecondary),
      title: Text(title),
      subtitle: subtitle != null ? Text(subtitle) : null,
      trailing: const Icon(Icons.chevron_right, color: AppColors.textSecondary),
      onTap: onTap,
    );
  }

  Widget _buildSwitchItem(
    BuildContext context, {
    required IconData icon,
    required String title,
    required bool value,
    required ValueChanged<bool> onChanged,
  }) {
    return ListTile(
      leading: Icon(icon, color: AppColors.textSecondary),
      title: Text(title),
      trailing: Switch(
        value: value,
        onChanged: onChanged,
        activeTrackColor: AppColors.primary,
      ),
    );
  }

  void _showSpeedDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('播放速度'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [0.5, 0.75, 1.0, 1.25, 1.5, 2.0].map((speed) {
            return ListTile(
              title: Text('${speed}x'),
              onTap: () {
                Navigator.pop(context);
              },
            );
          }).toList(),
        ),
      ),
    );
  }

  void _showTimerDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('自动关闭时长'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [15, 30, 45, 60, 90].map((minutes) {
            return ListTile(
              title: Text('$minutes 分钟'),
              onTap: () {
                Navigator.pop(context);
              },
            );
          }).toList(),
        ),
      ),
    );
  }

  void _showClearCacheDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('清除缓存'),
        content: const Text('确定要清除缓存吗？'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('取消'),
          ),
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('缓存已清除')),
              );
            },
            child: const Text('确定'),
          ),
        ],
      ),
    );
  }

  void _logout(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('退出登录'),
        content: const Text('确定要退出登录吗？'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('取消'),
          ),
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              context.read<AuthProvider>().logout();
            },
            child: const Text('确定'),
          ),
        ],
      ),
    );
  }
}
