export const PageHeader = ({ eyebrow, title, description, action, stat, image = "/dashboard-hero.png" }) => {
  return (
    <header
      className="page-hero mb-7 overflow-hidden rounded-[1.75rem] p-5 text-[var(--text-light)] sm:p-7 lg:p-8"
      style={{ "--page-hero-image": `url("${image}")` }}
    >
      <div className="relative z-10 grid gap-5 lg:grid-cols-[1fr_auto] lg:items-end">
        <div className="max-w-3xl">
          <div className="mb-3 flex items-center gap-2 text-[11px] font-black uppercase tracking-[0.18em] text-[var(--accent-soft)]">
            <span className="h-1 w-8 rounded-full bg-[var(--accent)]" />
            {eyebrow}
          </div>
          <h1 className="text-3xl font-black leading-tight tracking-normal text-white sm:text-4xl lg:text-5xl">
            {title}
          </h1>
          {description && <p className="mt-3 max-w-2xl text-sm leading-6 text-white/68">{description}</p>}
        </div>
        <div className="flex flex-col gap-3 sm:flex-row lg:flex-col lg:items-end">
          {stat && (
            <div className="rounded-2xl border border-white/10 bg-white/10 px-4 py-3 backdrop-blur">
              <p className="text-[10px] font-black uppercase tracking-[0.16em] text-white/48">{stat.label}</p>
              <p className="mt-1 text-2xl font-black text-white">{stat.value}</p>
            </div>
          )}
          {action}
        </div>
      </div>
    </header>
  );
};
