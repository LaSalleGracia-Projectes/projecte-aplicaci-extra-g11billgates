<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('likes', function (Blueprint $table) {
            $table->unsignedInteger('IDUsuario1')->change();
            $table->unsignedInteger('IDUsuario2')->change();
        });
    }

    public function down(): void
    {
        Schema::table('likes', function (Blueprint $table) {
            $table->unsignedBigInteger('IDUsuario1')->change();
            $table->unsignedBigInteger('IDUsuario2')->change();
        });
    }
};
