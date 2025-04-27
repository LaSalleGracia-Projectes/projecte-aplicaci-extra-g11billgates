<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::rename('chat', 'chats');
    }

    public function down(): void
    {
        Schema::rename('chats', 'chat');
    }
};
