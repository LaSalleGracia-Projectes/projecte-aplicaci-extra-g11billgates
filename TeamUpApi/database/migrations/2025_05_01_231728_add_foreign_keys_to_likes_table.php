<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('likes', function (Blueprint $table) {
            $table->foreign('IDUsuario1')->references('id')->on('users')->onDelete('cascade');
            $table->foreign('IDUsuario2')->references('id')->on('users')->onDelete('cascade');
        });
    }

    public function down(): void
    {
        Schema::table('likes', function (Blueprint $table) {
            $table->dropForeign(['IDUsuario1']);
            $table->dropForeign(['IDUsuario2']);
        });
    }
};
