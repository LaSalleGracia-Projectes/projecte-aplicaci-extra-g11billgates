<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::rename('matchusers', 'match_users');
    }

    public function down(): void
    {
        Schema::rename('match_users', 'matchusers');
    }
};
